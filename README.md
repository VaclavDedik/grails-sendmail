# Grails Sendmail

This plugin provides a simple interface for sending emails. The difference from Grails Mail plugin is that you can implement your own implementation that takes care of sending emails. All you have to do is implement DeliveryInterface and set this in your project Config.groovy:

    grails.mail.deliveryBean = "deliveryInterfaceImplementationBean"

This plugin currently only implements delivery bean for MailGun.