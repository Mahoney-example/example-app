package uk.org.lidalia
package exampleapp.server.domain

case class OutboundAdapters(
  emailService: EmailService,
  userProfileService: UserProfileRepository
)
