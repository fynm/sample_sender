node{
    checkout scm
    env.PATH = "${tool 'Maven3'}/bin:${env.PATH}"
    stage('PACKAGE'){
        dir('samp_send'){
           sh 'mvn clean compile assembly:single' 
        }
    }
    

    stage('Create Docker Image'){
        dir('samp_send'){
            docker.build("fynm/sample_sender:${env.BUILD_NUMBER}")
        }
    }

    stage('Run Application'){
        try{
            sh "docker run -it fynm/sample_sender:${env.BUILD_NUMBER}"
        }catch(error){
        }finally{

        }
    }



}