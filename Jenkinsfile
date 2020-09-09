#!groovy
import groovy.io.FileType
def getVersionsLib() {
    def metadata = new XmlSlurper().parse('http://18.159.141.245:8081/nexus/content/repositories/releases/hw/libs/common/helloworldlib/maven-metadata.xml')
    def versions = metadata.depthFirst().findAll { it.name() == 'version' }
    return versions.reverse()
}
def getSubProjects() {
    /* groovylint-disable-next-line NoDef */
    // def currentDir = new File('.')
    def currentDir = getClass().protectionDomain.codeSource.location.path
    def dirs = []
    currentDir.eachFile FileType.DIRECTORIES, {
        dirs << it.name
    }
    println dirs
}
pipeline {
    agent {
        label('agent')
    }
    options {
        timestamps()
    }
    parameters {
        booleanParam defaultValue: false, description: 'Building All Apps', name: 'BuildAllApp'
        choice(name: 'VERSION_LIB', choices: getVersionsLib(), description: 'Choise Library Versions')
    }
    stages {
        stage('Prepare Ws') {
            steps {
                cleanWs()
            }
        }
        stage('Git clone') {
            steps {
                git 'https://github.com/ausard/Hello-World-Lamda-java.git'
            }
        }
        stage('Build application') {
            steps {

                getSubProjects()


                dir('aws-hello-world-function') {
                    sh "./gradlew setLibVersion -PlibVersion=${params.VERSION_LIB}"
                    sh './gradlew clean build'
                }
                script {
                    if (params.BuildAllApp == true) {
                        dir('aws-hello-world-function-new') {
                            sh "./gradlew setLibVersion -PlibVersion=${params.VERSION_LIB}"
                            sh './gradlew clean build'
                        }
                    }
                }
            }
        }
        stage('Deploy the application') {
            steps {
                withCredentials([[$class           : 'AmazonWebServicesCredentialsBinding',
                                  accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                  credentialsId    : 'aws',
                                  secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                    sh 'export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID'
                    sh 'export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY'
                    script {
                        if (params.BuildAllApp == true) {
                            sh '/usr/local/bin/sam package --template-file template_all_app.yml --output-template-file packaged.yml --s3-bucket sam-deployment-bucket-ausard'
                            sh '/usr/local/bin/sam deploy --template-file packaged.yml'
                        } else {
                            sh '/usr/local/bin/sam package --template-file template_p.yml --output-template-file packaged.yml --s3-bucket sam-deployment-bucket-ausard'
                            sh '/usr/local/bin/sam deploy --template-file packaged.yml'
                        }
                    }
                }
            }
        }
    }
}
