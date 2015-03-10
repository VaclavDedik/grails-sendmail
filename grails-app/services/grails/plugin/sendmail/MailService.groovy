package grails.plugin.sendmail

class MailService {

    def grailsApplication
    def groovyPageRenderer

    def pluginConfig
    Mail mail

    def sendMail(Closure closure) {
        pluginConfig = grailsApplication.config.grails.mail
        mail = new Mail()
        def defaultFrom = grailsApplication.config.grails.mail.from
        def defaultReplyTo = grailsApplication.config.grails.mail.replyTo
        if (defaultFrom) {
            from(defaultFrom)
        }
        if (defaultReplyTo) {
            replyTo(defaultReplyTo)
        }

        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.call()

        if (!mail.to) {
            throw new IllegalArgumentException("Mail field 'to' is required.")
        }

        String serviceName = grailsApplication.config.grails.mail.deliveryBean
        DeliveryInterface service = (DeliveryInterface) grailsApplication.mainContext.getBean(serviceName)

        def overrideAddress = grailsApplication.config.grails.mail.overrideAddress
        if (overrideAddress) {
            to(overrideAddress)
        }

        service.sendMail(mail)
    }

    def to(CharSequence... to) {
        mail.to = to.collect([], {it.toString()})
    }

    def from(CharSequence from) {
        mail.from = from.toString()
    }

    def replyTo(CharSequence replyTo) {
        mail.replyTo = replyTo.toString()
    }

    def cc(CharSequence... cc) {
        mail.cc = cc.collect([], {it.toString()})
    }

    def bcc(CharSequence... bcc) {
        mail.bcc = bcc.collect([], {it.toString()})
    }

    def subject(CharSequence subject) {
        mail.subject = subject.toString()
    }

    def body(CharSequence body) {
        mail.body = body.toString()
    }

    def body(Map params) {
        mail.body = groovyPageRenderer.render(view: params["view"], model: params["model"])
    }

    def text(CharSequence text) {
        body(text)
    }

    def text(Map params) {
        body(params)
    }

    def html(CharSequence html) {
        body(html)
        mail.type = Mail.Type.HTML
    }

    def html(Map params) {
        body(params)
        mail.type = Mail.Type.HTML
    }
}
