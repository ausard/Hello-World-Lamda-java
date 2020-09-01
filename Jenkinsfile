#!groovy
pipeline {
    agent any
    options{
        timestamps()
    }
    envorinment{
        NEXUS_ADDRESS = 'http://18.159.141.245:8081/nexus/'
    }
    parameters {
        booleanParam defaultValue: false, description: 'Building All Apps', name: 'BuildAllApp'
    }
    stages {
        stage("Prepare Ws"){
            steps{
                cleanWs()
            }
        }
        stage("Git clone"){
            steps{
                git 'https://github.com/ausard/Hello-World-Lamda-java.git'
            }
        }
        stage("Build lib and publish to the nexus"){
            steps{
                dir("HelloWorldFunctionLibs"){
                    sh "echo nexus_addess=${NEXUS_ADDRESS} >> gradle.properties"
                    sh './gradlew clean build publish'
                }                
            }
        }
        stage("Build application"){
            steps{                
                dir("HelloWorldFunction"){
                    sh "echo ${NEXUS_ADDRESS} >> gradle.properties"
                    sh './gradlew clean build'
                }
                script{
                    if (params.BuildAllApp == true){
                      dir("HelloWorldFunctionNew"){
                          sh "echo ${NEXUS_ADDRESS} >> gradle.properties"
                          sh './gradlew clean build'
                      }
                    }
                }
            }
        }        
        stage("Deploy the application"){
            steps{
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                   accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                   credentialsId: 'aws',
                   secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        sh 'export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID'
                        sh 'export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY'
                        script{
                            if (params.BuildAllApp == true){
                                sh '/usr/local/bin/sam package --template-file template_all_app.yml --output-template-file packaged.yml --s3-bucket sam-deployment-bucket-ausard'
 	                              sh '/usr/local/bin/sam deploy --template-file packaged.yml'
                            }
                            else{
                                sh '/usr/local/bin/sam package --template-file template_p.yml --output-template-file packaged.yml --s3-bucket sam-deployment-bucket-ausard'
 	                            sh '/usr/local/bin/sam deploy --template-file packaged.yml'
                            }
                        }
                    }
            }
        }
    }
}
