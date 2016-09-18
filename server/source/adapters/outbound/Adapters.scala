package uk.org.lidalia
package exampleapp.server.adapters.outbound

import exampleapp.server.domain.{EmailService, UserProfileService}

case class Adapters(
  emailService: EmailService,
  userProfileService: UserProfileService
)
