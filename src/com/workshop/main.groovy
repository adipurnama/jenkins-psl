#!/usr/bin/groovy
package com.workshop

import com.workshop.Config
import com.workshop.stages.*

def main(script) {
    // Object initialization
   c = new Config()
   s_pre_build = new prebuild()
   s_build = new build()
   s_post_build = new postbuild()
   s_deploy = new deploy()
   s_post_deploy = new postdeploy()

   // Pipeline specific variable get from injected env
   // Mandatory variable will be check at details & validation steps
   def repository_name = ("${script.env.repository_name}" != "null") ? "${script.env.repository_name}" : ""
   def branch_name = ("${script.env.branch_name}" != "null") ? "${script.env.branch_name}" : ""
   def git_user = ("${script.env.git_user}" != "null") ? "${script.env.git_user}" : ""
   def docker_user = ("${script.env.docker_user}" != "null") ? "${script.env.docker_user}" : ""
   def app_port = ("${script.env.app_port}" != "null") ? "${script.env.app_port}" : ""
   def pr_num = ("${script.env.pr_num}" != "null") ? "${script.env.pr_num}" : ""

   // Timeout for Healtcheck
   def timeout_hc = (script.env.timeout_hc != "null") ? script.env.timeout_hc : 10

   // Have default value
   def docker_registry = ("${script.env.docker_registry}" != "null") ? "${script.env.docker_registry}" : "${c.default_docker_registry}"

    // Object initialization

   // Initialize docker tools
   def dockerTool = tool name: 'docker', type: 'dockerTool'

   // Pipeline object
   p = new Pipeline(
       repository_name,
       branch_name,
       git_user,
       docker_user,
       app_port,
       pr_num,
       dockerTool,
       docker_registry,
       timeout_hc,
   )


    // Pipeline object

    ansiColor('xterm') {

       stage('Pre Build - Details') {
           s_pre_build.validation(p)
           s_pre_build.details(p)
       }

       stage('Pre Build - Checkout & Test') {
           s_pre_build.checkoutBuildTest(p)
       }

       stage('Build & Push Image') {
           s_build.build(p)
       }

       stage('Merge') {
           s_post_build.merge(p)
       }

       stage('Deploy') {
           s_deploy.deploy(p)
       }

       stage('Service Healthcheck') {
           s_post_deploy.healthcheck(p)
       }
    }
}

return this
