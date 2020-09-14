#!groovy

static def getVersionsLib() {
    def listVersionsLib = 'http://18.192.21.247:8081/nexus/content/repositories/releases/hw/libs/common/helloworldlib/maven-metadata.xml'
    def metadata = new XmlSlurper().parse(listVersionsLib)
    def versions = metadata.depthFirst().findAll { it.name() == 'version' }
    return versions.reverse()
}

def buildApp(project) {
    sh "./gradlew :${project}:clean :${project}:build"
}

def listProjects() {
    def process = "ls -d */ | grep aws".execute()
    process.in.eachLine { line ->
        println line
    }
}

def deployTemplate(startTemplate, packagedTemplate) {
    sh "/usr/local/bin/sam package --template-file ${startTemplate} --output-template-file ${packagedTemplate} --s3-bucket sam-deployment-bucket-ausard"
    sh "/usr/local/bin/sam deploy --template-file ${packagedTemplate}"
}

def choiceProject(isBuild) {
    script {
        switch (params.app) {
            case 'all':
                isBuild ?
                        script {
                            sh "./gradlew clean build"
                        } :
                        deployTemplate('tmpl-aws-hello-world-all.yml', 'packaged-all.yml')
                break
            default:
                isBuild ? buildApp(params.app) : deployTemplate("tmpl-${params.app}.yml", "${params.app}-packaged.yml")
                break
        }
    }
}

pipeline {
    agent {
        label('aws_agent')
    }
    options {
        timestamps()
    }
    parameters {
        choice(name: 'VERSION_LIB', choices: getVersionsLib(), description: 'Choise library version')
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
                listProjects()
            }
        }
        stage('Set lib version for applications') {
            steps {
                sh "./gradlew setLibVersion -PlibVersion=${params.VERSION_LIB}"
            }
        }
        stage('Build application') {
            steps {
                choiceProject(true)
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
                    choiceProject(false)
                }
            }
        }
    }
}
