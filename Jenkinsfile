#!groovy
/* groovylint-disable DuplicateMapLiteral, DuplicateStringLiteral, ImplicitClosureParameter, LineLength, NestedBlockDepth, NoDef, UnnecessaryGetter */
def getVersionsLib() {
    def metadata = new XmlSlurper().parse('http://18.159.141.245:8081/nexus/content/repositories/releases/hw/libs/common/helloworldlib/maven-metadata.xml')    
    @NonCPS
    def versions = metadata.depthFirst().findAll { it.name() == 'version' }
    return versions.reverse()
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
                // echo env.VERSION_LIB
                dir('aws-hello-world-function') {
                    script{
                        ant.propertyfile(file: 'gradle.properties') {
                            entry(key: 'version', value: env.VERSION_LIB)
                        }
                    }
                    sh './gradlew clean build'
                }
                script {
                    if (params.BuildAllApp == true) {
                        dir('aws-hello-world-function-new') {
                            script{
                                ant.propertyfile(file: 'gradle.properties') {
                                    entry(key: 'version', value: env.VERSION_LIB)
                                }
                    }
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
