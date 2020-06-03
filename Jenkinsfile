node{
    checkout scm
    env.PATH = "${tool 'Maven3'}/bin:${env.PATH}"
    stage('PACKAGE'){
        dir('samp_send'){
           bat 'mvn clean compile assembly:single' 
        }
    }
    

    stage('Create Docker Image'){
        dir('samp_send'){
            docker.build("fynm/sample_sender:${env.BUILD_NUMBER}")
        }
    }

    stage('Run Application'){
        try{
            bat "DB=`docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' db`"
            bat "docker run -e DB_URI=$DB fynm/sample_sender:${env.BUILD_NUMBER}"
        }catch(error){
        }finally{

        }
    }



}