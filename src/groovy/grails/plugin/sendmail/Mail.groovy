package grails.plugin.sendmail

/**
 * @author vdedik@redhat.com
 */
class Mail {
    List<String> to
    String from
    String replyTo
    List<String> cc
    List<String> bcc
    String subject
    String body
    Type type = Type.TEXT

    enum Type {
        HTML, TEXT
    }
}
