package uk.org.lidalia.exampleapp.system.process

case class ProcessStatus(status: Int) {
  def isError: Boolean = 0 < status
  def isSuccess: Boolean = status == 0
}
