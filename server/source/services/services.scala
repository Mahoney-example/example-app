package uk.org.lidalia.exampleapp.server.services

import email.EmailService
import profiles.UserProfileService

case class Services(
  emailService: EmailService,
  userProfileService: UserProfileService
)
