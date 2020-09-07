#!groovy
pipeline {
    agent {
        label("agent")
    }
    options {
        timestamps()
    }
    parameters {
        booleanParam defaultValue: false, description: 'Building All Apps', name: 'BuildAllApp'
    }
    stages {        
        stage("Gather Deployment Parameters") {
            steps {
                timeout(time: 30, unit: 'SECONDS') {
                    script {
                        def metadata = new XmlSlurper().parse("http://18.159.141.245:8081/nexus/content/repositories/releases/hw/libs/common/helloworldlib/maven-metadata.xml")
                        def versions = []
                        for (version in metadata.versioning.versions) {
                            versions.add(version)
                        }
                        println versions
                    //     // Show the select input modal
                    //    def INPUT_PARAMS = input message: 'Please Provide Parameters', ok: 'Next',
                    //                     parameters: [
                    //                     choice(name: 'ENVIRONMENT', choices: ['dev','qa'].join('\n'), description: 'Please select the Environment'),
                    //                     choice(name: 'IMAGE_TAG', choices: getDockerImages(), description: 'Available Docker Images')]
                    //     env.ENVIRONMENT = INPUT_PARAMS.ENVIRONMENT
                    //     env.IMAGE_TAG = INPUT_PARAMS.IMAGE_TAG
                    }
                }
            }
        }
        stage("Prepare Ws") {
            steps {
                cleanWs()
            }
        }
        stage("Git clone") {
            steps {
                git 'https://github.com/ausard/Hello-World-Lamda-java.git'
            }
        }        
        stage("Build application") {
            steps {
                dir("HelloWorldFunction") {
                    sh './gradlew clean build'
                }
                script {
                    if (params.BuildAllApp == true) {
                        dir("HelloWorldFunctionNew") {
                            sh './gradlew clean build'
                        }
                    }
                }
            }
        }
        stage("Deploy the application") {
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
