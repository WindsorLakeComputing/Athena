export default class Certificate {
  public name: string
  public id: number

  constructor(name: string, id: number = 0) {
    this.name = name
    this.id = id
  }
}
