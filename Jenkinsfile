#!groovy

@NonCPS
def getVersionsLib() {
    /* groovylint-disable-next-line LineLength */
    def metadata = new XmlSlurper().parse('http://18.159.141.245:8081/nexus/content/repositories/releases/hw/libs/common/helloworldlib/maven-metadata.xml')
    def versions = metadata.depthFirst().findAll { it.name() == 'version' }
    return versions.reverse()
}

/* groovylint-disable-next-line CompileStatic */
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
        /* groovylint-disable-next-line LineLength */
        choice(name: 'app', choices: ['aws-hello-world-function', 'aws-hello-world-function-new', 'all'], description: 'choose which application to deploy')
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
                script {
                    /* groovylint-disable-next-line CouldBeSwitchStatement */
                    if (params.app == 'aws-hello-world-function') {
                        dir('aws-hello-world-function') {
                            sh "./gradlew setLibVersion -PlibVersion=${params.VERSION_LIB}"
                            sh './gradlew clean build'
                        }
                    }
                    if (params.app == 'aws-hello-world-function-new') {
                        dir('aws-hello-world-function-new') {
                            sh "./gradlew setLibVersion -PlibVersion=${params.VERSION_LIB}"
                            sh './gradlew clean build'
                        }
                    }
                    if (params.app == 'all') {
                        dir('aws-hello-world-function') {
                            sh "./gradlew setLibVersion -PlibVersion=${params.VERSION_LIB}"
                            sh './gradlew clean build'
                        }
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
                    /* groovylint-disable-next-line NestedBlockDepth */
                    script {
                        /* groovylint-disable-next-line DuplicateStringLiteral, NestedBlockDepth */
                        if (params.app == 'aws-hello-world-function') {
                            /* groovylint-disable-next-line LineLength */
                            sh '/usr/local/bin/sam package --template-file tmpl-aws-hello-world-function.yml --output-template-file packaged.yml --s3-bucket sam-deployment-bucket-ausard'
                            sh '/usr/local/bin/sam deploy --template-file packaged.yml'
                        }
                        /* groovylint-disable-next-line DuplicateStringLiteral, NestedBlockDepth */
                        if (params.app == 'aws-hello-world-function-new') {
                            /* groovylint-disable-next-line LineLength */
                            sh '/usr/local/bin/sam package --template-file tmpl-aws-hello-world-function-new.yml --output-template-file packaged-new.yml --s3-bucket sam-deployment-bucket-ausard'
                            /* groovylint-disable-next-line DuplicateStringLiteral */
                            sh '/usr/local/bin/sam deploy --template-file packaged-new.yml'
                        }
                        if (params.app == 'all') {
                            /* groovylint-disable-next-line LineLength */
                            sh '/usr/local/bin/sam package --template-file tmpl-aws-hello-world-all.yml --output-template-file packaged-all.yml --s3-bucket sam-deployment-bucket-ausard'
                            sh '/usr/local/bin/sam deploy --template-file packaged-all.yml'                            
                        }
                    }
                }
            }
        }
    }
}
