#!/usr/bin/groovy
package com.workshop

class Config {
    // Place Default Variable Here
    // Docker related default variable
   def default_docker_registry = "https://registry-1.docker.io/v2/"
   def default_docker_registry_jenkins_cred = 'dh-adipurnama'
   def default_docker_jenkins_tool = 'docker'

   // Golang related default variable
   def default_golang_base_image = "tobapramudia/tkpd-demo:onbuild"
}
