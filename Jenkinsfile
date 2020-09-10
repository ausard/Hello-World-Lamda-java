#!groovy

@NonCPS
def getVersionsLib() {
    def listVersionsLib = 'http://18.159.141.245:8081/nexus/content/repositories/releases/hw/libs/common/helloworldlib/maven-metadata.xml'
    def metadata = new XmlSlurper().parse(listVersionsLib)
    def versions = metadata.depthFirst().findAll { it.name() == 'version' }
    return versions.reverse()
}
def buildApp(dirName){
    dir(dirName){
        sh "./gradlew setLibVersion -PlibVersion=${params.VERSION_LIB}"
        sh './gradlew clean build'
    }    
}
def deployTemplate(startTemlpate, packagedTemplate){
    sh "/usr/local/bin/sam package --template-file ${startTemlpate} --output-template-file ${packagedTemplate} --s3-bucket sam-deployment-bucket-ausard"
    sh "/usr/local/bin/sam deploy --template-file ${packagedTemplate}"
}
def choiceBuildProject(){
    script{
        switch(params.app) {
            case 'aws-hello-world-function':
                buildApp('aws-hello-world-function')
                break
            case 'aws-hello-world-function-new':
                buildApp('aws-hello-world-function-new')
                break
            case 'all':
                buildApp('aws-hello-world-function')
                buildApp('aws-hello-world-function-new')
                break            
        }
    }
}
pipeline {
    agent {
        label('agent')
    }
    options {
        timestamps()
    }
    parameters {
        choice(name: 'VERSION_LIB', choices: getVersionsLib(), description: 'Choise Library Versions')
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
                choiceBuildProject(){
                // script {
                //     if (params.app == 'aws-hello-world-function') {
                //         buildApp('aws-hello-world-function')
                //     }
                //     if (params.app == 'aws-hello-world-function-new') {
                //         buildApp('aws-hello-world-function-new')
                //     }
                //     if (params.app == 'all') {
                //         buildApp('aws-hello-world-function')
                //         buildApp('aws-hello-world-function-new')
                //     }
                // }
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
                        if (params.app == 'aws-hello-world-function') {
                            deployTemplate('tmpl-aws-hello-world-function.yml', 'packaged.yml')                            
                        }
                        if (params.app == 'aws-hello-world-function-new') {
                            deployTemplate('tmpl-aws-hello-world-function-new.yml', 'packaged-new.yml')
                        }
                        if (params.app == 'all') {
                            deployTemplate('tmpl-aws-hello-world-all.yml', 'packaged-all.yml')
                        }
                    }
                }
            }
        }
    }
}
