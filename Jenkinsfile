pipeline{
    agent any
    tools{
        maven 'LOCALMAVEN'
    }
    stages{
        stage("Build maven project"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/re-gis/blue_ms_api']])
                sh "mvn clean install"
            }
        }
        
        stage("Build docker image"){
            steps{
                script{
                    sh "docker build -t dregismerci/blue-mis ."
                }
            }
        }
        
        stage("Push image to Dockerhub"){
            steps{
                script{
                    withCredentials([string(credentialsId: 'docker_pwd', variable: 'docker_pwd')]) {
                        sh "docker login -u dregismerci -p ${docker_pwd}"
                    }
                    
                    sh "docker push dregismerci/blue-mis"
                }
            }
        }
    }
}