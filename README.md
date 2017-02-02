# Candijay Water Billing System

This application is currently used by the Municipality of Candijay, Bohol.

Features:
   * Manage Consumers' Information
   * Create, update, delete and view history of Readings
   * Automate bills creation and mass printing
   * Generate reports such as collectibles, collection.
   * Generate graphs
   * Save payment records, and generate accounts’ payment history
   * Generate notice of disconnection to accounts
   * Tracking of active and inactive accounts
   * Adjusting system variables e.g. rates, consecutive debts allowed, etc.
   * System users role-based access

Main Technologies Used:

Deployment:
   * Tomcat 8
Server-side:
   * Spring MVC.
   * Spring Security.
   * Dandelion DataTables
      * No longer maintained but still a very useful for apps using jQuery DataTables
      * supports JSP and Thymeleaf
   * Jasper Reports
   * Spring Data JPA
   * Hibernate
   * MySQL
 
Client-side:
   * Twitter bootstrap
   * jQuery DataTables
   * bootstrap-toggle
   * bootstrap-dialog
   * Chart.js

You can Download the WAR file [here](https://www.dropbox.com/s/nytl5cx9l585uiw/ROOT.war?dl=0) and deploy on your own Tomcat Server.

This project was deployed in OpenShift using `jbossews` cartridge documentation can be found at:
http://openshift.github.io/documentation/oo_cartridge_guide.html#tomcat
