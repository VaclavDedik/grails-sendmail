package grails.plugin.sendmail

import groovyx.net.http.HTTPBuilder

class MailGunService {

    def sendMail(Mail mail) {
        def uri = ""
        def http = new HTTPBuilder(uri)
        def postBody = [from: mail.from, to: mail.to, subject: mail.subject, text: mail.text]

    }
}
