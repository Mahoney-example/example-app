package uk.org.lidalia.exampleapp.system.process

case class ProcessOutput(
  status: ProcessStatus,
  out: String,
  err: String,
  all: String
)
