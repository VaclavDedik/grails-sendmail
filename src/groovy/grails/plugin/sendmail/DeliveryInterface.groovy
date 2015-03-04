package grails.plugin.sendmail

/**
 * @author vdedik@redhat.com
 */
interface DeliveryInterface {
    def sendMail(Mail mail)
}
