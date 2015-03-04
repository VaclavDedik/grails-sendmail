package grails.plugin.sendmail

import groovyx.net.http.HTTPBuilder

class MailGunService implements DeliveryInterface {

    def grailsApplication

    def sendMail(Mail mail) {
        def uri = grailsApplication.config.grails.mailgun.api.url
        def key = grailsApplication.config.grails.mailgun.api.key

        def http = new HTTPBuilder(uri)
        def postBody = [from: mail.from, to: mail.to, subject: mail.subject, text: mail.text]
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
        http.post(body: postBody) { resp ->
            if (resp.statusLine.statusCode / 100 == 2) {
                log.info("Mail successfully sent to ${mail.to}.")
            } else {
                log.error("Mail to ${mail.to} could not be sent.")
            }
        }
    }
}
