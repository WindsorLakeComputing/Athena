import React from "react"
import {RenderResult} from "react-testing-library"
import App from "../App"
import {MaintainerFactory} from "../factories/MaintainerFactory"
import RequestHelper from "../helpers/RequestHelper"
import {expect, fireEvent, render, sinon, wait} from "../testHelpers/importsHelper"
import {forIt} from "../testHelpers/testHelpers"

let container: HTMLElement
let rendered: RenderResult

describe("App", () => {
  const maintainer1 = MaintainerFactory.buildApiResponse({
    id: 1,
    employeeId: "1234",
    firstName: "Henry",
    lastName: "Arnold",
    absences: [{
      id: 1,
      hours: "",
      reason: "Bad Cough",
      startDate: "2019-05-14",
      endDate: "2019-05-14"
    }],
    section: {name: "Production"}
  })
  const maintainer2 = MaintainerFactory.buildApiResponse({
    id: 2,
    employeeId: "5678",
    section: {name: "APG"}
  })
  const maintainer3 = MaintainerFactory.buildApiResponse({
    id: 3,
    employeeId: "9012",
    firstName: "Carl",
    lastName: "Spaatz",
    section: {name: "Production"},
    certificates: []
  })
  const maintainers: any = {maintainers: [maintainer1, maintainer2, maintainer3]}

  const getStub = sinon.stub(RequestHelper, "get")

  beforeEach(() => {
    getStub.withArgs("/api/maintainers")
      .returns(Promise.resolve({
        errorStatus: false,
        body: maintainers
      }))

    getStub.withArgs("/api/user")
      .returns(Promise.resolve({
        errorStatus: false,
        body: {userName: "Hermes Weasley"}
      }))
  })

  afterEach(async () => {
    await forIt()
    getStub.reset()
  })

  it("renders without crashing", () => {
    rendered = render(<App/>)
    container = rendered.container
    expect(container).to.exist
  })

  describe("Maintainers table", () => {
    it("Displays maintainers from all sections", async () => {
      rendered = render(<App/>)
      container = rendered.container

      await forIt()
      expect(container.querySelectorAll(".maintainer").length).to.equal(3)
    })
  })
})
