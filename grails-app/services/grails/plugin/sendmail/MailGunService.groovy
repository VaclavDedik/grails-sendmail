package grails.plugin.sendmail

import groovyx.net.http.HTTPBuilder

class MailGunService implements DeliveryInterface {

    def grailsApplication

    def sendMail(Mail mail) {
        if (!mail.from) {
            log.error("Mail to ${mail.to} could not be sent. Missing from field.")
            throw new IllegalArgumentException("Mail field 'from' is required.")
        }

        def uri = grailsApplication.config.grails.mailgun.api.url
        def key = grailsApplication.config.grails.mailgun.api.key

        def http = new HTTPBuilder(uri)
        def postBody = [from: mail.from, to: mail.to, subject: mail.subject, body: mail.body]
        if (mail.cc) {
            postBody["cc"] = mail.cc
        }
        if (mail.bcc) {
            postBody["bcc"] = mail.bcc
        }
        if (mail.replyTo) {
            // TODO: Test if this really works
            postBody["h:Reply-To"] = mail.replyTo
        }

        http.auth.basic("api", key)
        http.handler.success = {
            log.info("Mail successfully sent to ${mail.to}.")
        }
        http.handler.failure = { resp ->
            log.error("Mail to ${mail.to} could not be sent. Status code: ${resp.statusCode}")
        }
        http.post(body: postBody)
    }
}
