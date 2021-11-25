/*
 * Vanessa-Usher
 * Copyright (C) 2019-2021 SilverBulleters, LLC - All Rights Reserved.
 * Unauthorized copying of this file in any way is strictly prohibited.
 * Proprietary and confidential.
 */
import com.cloudbees.groovy.cps.NonCPS
import groovy.transform.Field
import hudson.tasks.test.AbstractTestResultAction
import org.silverbulleters.usher.NotificationInfo
import org.silverbulleters.usher.config.PipelineConfiguration
import org.silverbulleters.usher.config.additional.NotificationMode
import org.silverbulleters.usher.state.PipelineState
import org.silverbulleters.usher.util.Common

/**
 * Конфигурация
 */
@Field
PipelineConfiguration config

/**
 * Состояние
 */
@Field
PipelineState state

/**
 * Информация для уведомления
 */
@Field
NotificationInfo notificationInfo = new NotificationInfo()

void call() {
  call('pipeline.json')
}

void call(String pathToConfig) {

  state = newPipelineState()

  def libraryVersion = Common.getLibraryVersion()
  print("Версия Vanessa.Usher: ${libraryVersion}")

  readConfig(pathToConfig)

  catchError(buildResult: 'FAILURE') {
    start()
  }

  notificationInfo.status = "${currentBuild.currentResult}"
  notificationInfo.showTestResults = common.needPublishTests(config)
  if (notificationInfo.showTestResults) {
    fillSummaryTestResults()
  }

  if (currentBuild.currentResult == 'SUCCESS') {
    if (!config.stages.gitsync) {
      sendNotification()
    }
  } else if (currentBuild.currentResult == 'FAILURE') {
    sendNotification()
  }
}

private void readConfig(String pathToConfig) {

  logger.info("Чтение конфигурационного файла")

  def absolutePathToConfig = common.getAbsolutePathToConfig(pathToConfig)
  def file = new File(absolutePathToConfig)
  if (file.exists()) {
    config = getPipelineConfiguration(absolutePathToConfig)
  } else {
    node {
      checkout scm
      config = getPipelineConfiguration(pathToConfig, true)
    }
  }
}

void start() {

  if (config.stages.gitsync) {
    startGitSync()
  } else {
    startBuildPipeline()
  }

}

private void startGitSync() {

  node(config.getAgent()) {

    def scmVariables = checkout scm
    fillNotificationInfo(scmVariables)

    stageGitsync(config)

  }

}

private void startBuildPipeline() {

  node(config.getAgent()) {
    def scmVariables = checkout scm
    fillNotificationInfo(scmVariables)

    stageEdtTransform(config)
    stagePrepareBase(config, state)
    stageSyntaxCheck(config, state)

    testing()

    stageSonarAnalyze(config)

    stageBuild(config, state)

    stageReportPublish(config, state)
  }

}

void testing() {

  if (config.matrixTesting.agents.size() == 0) {
    performTesting()
  } else {
    def jobs = [:]
    def count = 1
    config.matrixTesting.agents.each { agentName ->
      def name = "${count}. ${agentName}"
      jobs[name] = {
        node(agentName) {
          checkout scm
          performTesting()
        }
      }
      count++
    }

    parallel jobs
  }

}

void performTesting() {
  stageSmoke(config, state)
  stageTdd(config, state)
  stageBdd(config, state)
}

void sendNotification() {
  address = ""
  def providerNotification
  if (config.getNotification().getMode() == NotificationMode.SLACK) {
    providerNotification = slackHelper
    address = config.notification.slack.channelName
  } else if (config.getNotification().getMode() == NotificationMode.EMAIL) {
    providerNotification = emailHelper
    address = config.notification.email
  } else {
    return
  }

  if (config.stages.gitsync) {
    providerNotification.sendErrorNotification(address, notificationInfo)
  } else {
    providerNotification.sendNotification(address, notificationInfo)
  }
}

void fillNotificationInfo(scmVariables) {
  // для slack only
  notificationInfo.channelId = config.notification.slack.channelName
  notificationInfo.projectName = "${env.JOB_NAME}"
  notificationInfo.buildNumber = "${env.BUILD_NUMBER}"
  notificationInfo.buildUrl = "${env.BUILD_URL}"
  notificationInfo.branchName = "${scmVariables.GIT_BRANCH}"
  notificationInfo.commitId = "${scmVariables.GIT_COMMIT}"
  notificationInfo.commitMessage = ""
}

@NonCPS
void fillSummaryTestResults() {
  def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
  if (testResultAction == null) {
    notificationInfo.showTestResults = false
    return
  }
  notificationInfo.failedCount = testResultAction.getFailCount()
  notificationInfo.skippedCount = testResultAction.getSkipCount()
  notificationInfo.successCount = testResultAction.getTotalCount() - notificationInfo.failedCount - notificationInfo.skippedCount
}