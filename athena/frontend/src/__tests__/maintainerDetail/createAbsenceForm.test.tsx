import moment, {Moment} from "moment"
import React from "react"
import sinon from "sinon"
import CreateAbsenceForm from "../../components/maintainerDetail/CreateAbsenceForm"
import RequestHelper from "../../helpers/RequestHelper"
import {expect, fireEvent, render} from "../../testHelpers/importsHelper"

describe("Create Absence Form", () => {
  let container: HTMLElement

  const createAbsenceSpy = sinon.spy()

  const postStub = sinon.stub(RequestHelper, "postJson")

  beforeEach(() => {
    postStub.withArgs("/api/absences/create", sinon.match.any)
      .returns(Promise.resolve(false))

    container = render(
      <CreateAbsenceForm createAbsence={createAbsenceSpy}/>
    ).container
  })

  afterEach( () => {
    createAbsenceSpy.resetHistory()
    postStub.reset()
  })

  it("passes absence form values to createAbsence", async () => {
    const startDateField = container.querySelector("#start-date-id")
    await fireEvent.focus(startDateField!)

    const startDate = moment().format("dddd, MMMM D, Y")
    const endDate = moment().add(2, "days").format("dddd, MMMM D, Y")
    const startDateElement = container.querySelectorAll(`[aria-label="${startDate}"]`)[0]
    await fireEvent.click(startDateElement)

    const endDateElement = container.querySelectorAll(`[aria-label="${endDate}"]`)[0]
    await fireEvent.click(endDateElement)

    const reasonInput = container.querySelectorAll("#absence_description")[0]
    await fireEvent.change(reasonInput, {target: {value: "blahblah"}})

    const hoursInput = container.querySelectorAll("#absence_hours")[0]
    await fireEvent.change(hoursInput, {target: {value: "2100-2400"}})

    const locationInput = container.querySelectorAll("#absence_location")[0]
    await fireEvent.change(locationInput, {target: {value: "Vegas"}})

    const submitButton = container.querySelectorAll(".create_absence")[0]
    await fireEvent.click(submitButton)

    expect(createAbsenceSpy.callCount).to.eql(1)
    expect(parseStringDate(startDate).isSame(createAbsenceSpy.args[0][0], "day")).to.be.true
    expect(parseStringDate(endDate).isSame(createAbsenceSpy.args[0][1], "day")).to.be.true
    expect(createAbsenceSpy.args[0][2]).to.eql("blahblah")
    expect(createAbsenceSpy.args[0][3]).to.eql("2100-2400")
    expect(createAbsenceSpy.args[0][4]).to.eql("Vegas")

  })

  describe("validation", () => {
    const startDateString = moment().format("dddd, MMMM D, Y")
    const endDateString = moment().add(2, "days").format("dddd, MMMM D, Y")

    describe("dates", () => {
      it("does not call createAbsence if no date", async () => {
        await enterNewAbsence(
          null,
          null,
          "Hootenanny",
          null,
          "2100-2400")
        const submitButton = container.querySelectorAll(".create_absence")[0]
        await fireEvent.click(submitButton)
        expect(createAbsenceSpy).to.not.have.been.called
      })

      it("does not call createAbsence if no end date", async () => {
        await enterNewAbsence(
          startDateString,
          null,
          "Hootenanny",
          null,
          "2100-2400")
        const submitButton = container.querySelectorAll(".create_absence")[0]
        await fireEvent.click(submitButton)
        expect(createAbsenceSpy).to.not.have.been.called
      })
    })

    describe("hours", () => {
      it("does not call createAbsence if hours invalid", async () => {
        await enterNewAbsence(
          startDateString,
          endDateString,
          "valid reason",
          null,
          "!@%!@$")
        const submitButton = container.querySelectorAll(".create_absence")[0]
        await fireEvent.click(submitButton)
        expect(createAbsenceSpy).to.not.have.been.called
      })

      it("renders error messages if hours invalid", async () => {
        await enterNewAbsence(
          startDateString,
          endDateString,
          "Valid Reason",
          null,
          "!@%@$#")

        expect(container.querySelector("#hours_error_message")!.textContent!.length).to.be.above(1)
      })

      it("does not display error messages when hours is valid", async () => {
        await enterNewAbsence(
          startDateString,
          endDateString,
          "rodeo",
          null,
          "2100-2400")

        expect(container.querySelectorAll(".error_message").length).to.eq(0)
      })
    })

    describe("reason", () => {
      it("does not call createAbsence if no reason", async () => {
        await enterNewAbsence(
          startDateString,
          endDateString,
          null,
          null,
          "2100-2400")
        const submitButton = container.querySelectorAll(".create_absence")[0]
        await fireEvent.click(submitButton)
        expect(createAbsenceSpy).to.not.have.been.called
      })

      it("does not call createAbsence if reason invalid", async () => {
        await enterNewAbsence(
          startDateString,
          endDateString,
          "%&@!",
          null,
          null)
        const submitButton = container.querySelectorAll(".create_absence")[0]
        await fireEvent.click(submitButton)
        expect(createAbsenceSpy).to.not.have.been.called
      })

      it("renders error messages if reason invalid", async () => {
        await enterNewAbsence(
          startDateString,
          endDateString,
          "%&@!",
          null,
          null)

        expect(container.querySelector("#reason_error_message")!.textContent!.length).to.be.above(1)
      })

      it("does not display error messages when the reason is valid", async () => {
        await enterNewAbsence(
          startDateString,
          endDateString,
          "rodeo",
          null,
          null)

        expect(container.querySelectorAll(".error_message").length).to.eq(0)
      })
    })

    describe("location", () => {
      beforeEach(async () => {
        await enterNewAbsence(
          startDateString,
          endDateString,
          "valid reason",
          "!@$@#",
          "2100-2400")
      })

      it("does not call createAbsence if location invalid", async () => {
        const submitButton = container.querySelectorAll(".create_absence")[0]
        await fireEvent.click(submitButton)
        expect(createAbsenceSpy).to.not.have.been.called
      })

      it("renders error messages if location invalid", async () => {

        expect(container.querySelector("#location_error_message")!.textContent!.length).to.be.above(1)
      })
    })
  })

  async function enterNewAbsence(
    startDate: string | null,
    endDate: string | null,
    reason: string | null,
    location: string | null,
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
    if (!!location) {
      const locationInput = container.querySelectorAll("#absence_location")[0]
      await fireEvent.change(locationInput, {target: {value: location}})
    }
    if (!!hours) {
      const hoursInput = container.querySelectorAll("#absence_hours")[0]
      await fireEvent.change(hoursInput, {target: {value: hours}})
    }
  }
})

const parseStringDate = (stringDate: string) => moment(new Date(stringDate))
