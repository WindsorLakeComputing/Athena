class MadHatterUser {
  public firstName: string
  public lastName: string
  public email: string
  public amu: string

  constructor(
    firstName: string,
    lastName: string,
    email: string,
    amu: string
  ) {
    this.firstName = firstName
    this.lastName = lastName
    this.email = email
    this.amu = amu
  }
}

export default MadHatterUser
