#!groovy
pipeline {
   agent any
   options{
      timestamps()
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

        stage("Gradle build"){
            steps{
                dir("HelloWorldFunctionLibs"){
                    sh './gradlew clean build publish'
                }
                dir("HelloWorldFunction"){
                    sh './gradlew clean build'
                }
                script{
                    if (params.BuildAllApp == true){
                      dir("HelloWorldFunctionNew"){
                          sh './gradlew clean build'
                      }
                    }
                }

            }
        }
        stage("Build and Deploy the application"){
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
