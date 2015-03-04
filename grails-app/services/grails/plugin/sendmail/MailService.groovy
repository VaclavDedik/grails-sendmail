package grails.plugin.sendmail

class MailService {

    def mailGunService

    Mail mail

    MailService() {

    }

    def send(Closure closure) {
        Mail mail = new Mail()

        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.call()

        mailGunService.sendMail(mail)
    }

    def to(String... to) {
        mail.to = to
    }

    def from(String from) {
        mail.from = from
    }

    def replyTo(String replyTo) {
        mail.replyTo = replyTo
    }

    def cc(String... cc) {
        mail.cc = cc
    }

    def bcc(String... bcc) {
        mail.bcc = bcc
    }

    def subject(String subject) {
        mail.subject = subject
    }

    def text(String text) {
        mail.text = text
    }

    def text(String view, Map model) {
        // Not implemented yet
    }
}
