import moment from "moment"
import React from "react"
import {MemoryRouter} from "react-router"
import MaintainerDetail from "../../components/maintainerDetail/MaintainerDetail"
import {AbsenceFactory} from "../../factories/AbsenceFactory"
import {MaintainerFactory} from "../../factories/MaintainerFactory"
import RequestHelper from "../../helpers/RequestHelper"
import Certificate from "../../models/Certificate"
import {Maintainer} from "../../models/Maintainer"
import Section from "../../models/Section"
import Shift from "../../models/Shift"
import {expect, fireEvent, render, sinon} from "../../testHelpers/importsHelper"
import {forIt} from "../../testHelpers/testHelpers"

describe("Maintainer Detail", () => {
  const maintainerCerts: Certificate[] = [new Certificate("great cert")]
  let defaultMaintainer: Maintainer

  const getStub = sinon.stub(RequestHelper, "get")
  const postStub = sinon.stub(RequestHelper, "postJson")
  const deleteStub = sinon.stub(RequestHelper, "delete")

  beforeEach(() => {
    defaultMaintainer = new Maintainer(
      1,
      "Bob",
      "Skywalker",
      "1",
      new Shift("D"),
      new Shift("S"),
      new Shift("M"),
      maintainerCerts,
      7,
      "TSgt",
      new Shift("D"),
      new Section("Weapons"),
      [],
      [])

    getStub.withArgs(`/api/absences/maintainer/1`)
      .returns(Promise.resolve({
          errorStatus: false,
          body: {
            absences: [{
              id: 1, startDate: "2019-05-14", endDate: "2019-05-14",
              maintainer: 1, reason: "Rodeo", location: "El Paso", hours: "2100-2400"
            }]
          }
        }
      ))

    getStub.withArgs(`/api/absences/maintainer/2`)
      .returns(Promise.resolve({
          errorStatus: false,
          body: {
            absences: []
          }
        }
      ))
  })

  afterEach(() => {
    getStub.reset()
    postStub.reset()
    deleteStub.reset()
  })

  const renderMaintainerDetail = (maintainer = defaultMaintainer) => (
    render(
      <MemoryRouter>
        <MaintainerDetail maintainer={maintainer} reloadMaintainer={() => null}/>
      </MemoryRouter>
    )
  )

  describe("displaying maintainer details", () => {
    it("displays maintainer header", async () => {

      const {container} = renderMaintainerDetail()

      const subjectDiv = container.querySelector(".maintainer-details h1")
      expect(subjectDiv!.textContent).equal("Bob Skywalker TSgt")

    })

    it("displays maintainer certs and level", () => {
      const {container} = renderMaintainerDetail()

      const subjectDiv = container.querySelector(".maintainer-attributes")
      expect(subjectDiv!.textContent).to.eq("7 D  great cert D S M")
    })

  })

  it("displays a red x/limited red x form", () => {
    const {container} = renderMaintainerDetail()

    expect(container.querySelector("#red-x-radio")).to.exist
    expect(container.querySelector("#limited-red-x-radio")).to.exist
  })

  describe("absences display", () => {
    it("displays maintainer absence info in a table view", async () => {
      const testMaintainer = MaintainerFactory.build({
        absences: [
          AbsenceFactory.build()
        ]
      })
      const {container} = renderMaintainerDetail(testMaintainer)

      await forIt()

      expect(container.querySelectorAll("table").length).to.eq(1)
    })
    it("does not display the absence display when there are no absences", async () => {
      const testMaintainer = MaintainerFactory.build()
      const {container} = renderMaintainerDetail(testMaintainer)

      await forIt()

      expect(container.querySelector(".absence-list-title")).to.not.exist
    })
  })

  describe("Adding absence to maintainer", () => {
    let container: HTMLElement

    beforeEach(() => {
      postStub.withArgs("/api/absences/create", sinon.match.any)
        .returns(Promise.resolve(false))

      container = renderMaintainerDetail().container
    })

    it("posts a absence given correct input on clicking submit", async () => {
      await enterNewAbsence(
        moment().format("dddd, MMMM D, Y"),
        moment().add(2, "days").format("dddd, MMMM D, Y"),
        "Hootenanny",
        "2100-2400")
      const submitButton = container.querySelectorAll(".create_absence")[0]
      await fireEvent.click(submitButton)
      expect(postStub).to.have.been.calledOnce
    })

    async function enterNewAbsence(
      startDate: string | null,
      endDate: string | null,
      reason: string | null,
      hours: string | null
    ) {
      if (!!startDate) {
        const startDateEntry = container.querySelectorAll("#start-date-id")[0]
        await fireEvent.focus(startDateEntry)
        expect(container.querySelectorAll("[aria-label=\"Calendar\"]").length).to.eq(1)
        const startDateElement = container.querySelectorAll(`[aria-label="${startDate}"]`)[0]
        await fireEvent.click(startDateElement)
        if (!!endDate) {
          const endDateElement = container.querySelectorAll(`[aria-label="${endDate}"]`)[0]
          await fireEvent.click(endDateElement)
        }
      }
      if (!!reason) {
        const reasonInput = container.querySelectorAll("#absence_description")[0]
        await fireEvent.change(reasonInput, {target: {value: reason}})
      }
      if (!!hours) {
        const reasonInput = container.querySelectorAll("#absence_hours")[0]
        await fireEvent.change(reasonInput, {target: {value: hours}})
      }
    }
  })

  describe("Modifying red X certs on a maintainers", () => {
    it("makes a put request with the maintainer containing selected cert", async () => {
      const putStub = sinon.stub(RequestHelper, "put")
      putStub.withArgs("/api/maintainer/1", sinon.match.any)
        .returns(Promise.resolve(false))

      const {container} = renderMaintainerDetail()

      const redXRadio = container.querySelector("#red-x-radio")
      if (redXRadio) {
        await fireEvent.click(redXRadio)
      }

      expect(putStub).to.have.been.calledOnce
      expect(putStub.getCall(0).lastArg.certificates.length).to.eq(2)
      expect(putStub.getCall(0).lastArg.certificates[1].name).to.eq("X")
    })
  })

  describe("displays Schedule and Certification tabs", () => {
    it("contains 2 tabs", () => {
      const {container} = renderMaintainerDetail()

      expect(container.querySelector(".schedule_tab")).to.exist
      expect(container.querySelector(".certifications_tab")).to.exist
    })

    it("displays certifications when certifications tab is clicked", async () => {
      const testMaintainer = MaintainerFactory.build({
        certificates: [new Certificate("Run")]
      })
      const {container} = renderMaintainerDetail(testMaintainer)

      const certificationsTab = container.querySelector(".certifications_tab")
      if (certificationsTab) {
        await fireEvent.click(certificationsTab)
      }

      const checkboxes = container.querySelectorAll(".certification_checkbox input")
      expect(checkboxes.length).to.eq(5)
      expect(checkboxes[0]).to.have.property("checked", false)
      expect(checkboxes[1]).to.have.property("checked", true)
    })
  })
})
