import moment from "moment"
import React from "react"
import {MemoryRouter} from "react-router"
import {RenderResult} from "react-testing-library"
import sinon from "sinon"
import MaintainerList from "../../components/scheduleView/MaintainerList"
import {AbsenceFactory} from "../../factories/AbsenceFactory"
import {MaintainerFactory} from "../../factories/MaintainerFactory"
import RequestHelper from "../../helpers/RequestHelper"
import Certificate from "../../models/Certificate"
import Shift from "../../models/Shift"
import {Standby} from "../../models/Standby"
import {expect, fireEvent, render} from "../../testHelpers/importsHelper"
import {forIt} from "../../testHelpers/testHelpers"

describe("maintainerList", () => {
  let container: HTMLElement
  let rendered: RenderResult

  const maintainer1 = MaintainerFactory.build({id: 1})
  const maintainer2 = MaintainerFactory.build({id: 2})

  it("displays maintainer info in a tableView", () => {
    rendered = render(
      <MemoryRouter>
        <MaintainerList maintainers={[maintainer1, maintainer2]} date={moment()} reloadMaintainer={() => null}/>
      </MemoryRouter>
    )
    container = rendered.container
    expect(container.querySelectorAll("table").length).to.eq(1)
  })

  it("displays a column for the 7 certs we currently care about", () => {
    rendered = render(
      <MemoryRouter>
        <MaintainerList maintainers={[]} date={moment()} reloadMaintainer={() => null}/>
      </MemoryRouter>
    )
    container = rendered.container

    expect(getColumnHeaders()[4].textContent).to.equal("X")
    expect(getColumnHeaders()[5].textContent).to.equal("L/X")
    expect(getColumnHeaders()[6].textContent).to.equal("I/E")
    expect(getColumnHeaders()[7].textContent).to.equal("Run")
    expect(getColumnHeaders()[8].textContent).to.equal("HP")
    expect(getColumnHeaders()[9].textContent).to.equal("TS")
    expect(getColumnHeaders()[10].textContent).to.equal("Bore")
  })

  it("displays certification availability based on presence of a certificate", () => {

    const maintainer3 = MaintainerFactory.build({id: 3})
    maintainer3.certificates = [new Certificate("X"), new Certificate("L/X"), new Certificate("I/E")]
    rendered = render(
      <MemoryRouter>
        <MaintainerList maintainers={[maintainer1, maintainer3]} date={moment()} reloadMaintainer={() => null}/>
      </MemoryRouter>
    )
    container = rendered.container

    // fix this to pull the img elements
    expect(getMaintainerFields(0)[4].querySelectorAll("img").length).to.equal(1)
    expect(getMaintainerFields(0)[5].querySelectorAll("img").length).to.equal(0)
    expect(getMaintainerFields(0)[6].querySelectorAll("img").length).to.equal(0)

    expect(getMaintainerFields(1)[4].querySelectorAll("img").length).to.equal(1)
    expect(getMaintainerFields(1)[5].querySelectorAll("img").length).to.equal(1)
    expect(getMaintainerFields(1)[6].querySelectorAll("img").length).to.equal(1)
  })

  it("Has headers for the 5 week days and one header for the weekend days", () => {
    rendered = render(
      <MemoryRouter>
        <MaintainerList maintainers={[]}
                        date={moment().set({year: 2019, month: 4, date: 15})}
                        reloadMaintainer={() => null}/>
      </MemoryRouter>
    )
    container = rendered.container

    expect(getColumnHeaders()[11].textContent).to.eq("13, May")
    expect(getColumnHeaders()[12].textContent).to.eq("14, May")
    expect(getColumnHeaders()[13].textContent).to.eq("15, May")
    expect(getColumnHeaders()[14].textContent).to.eq("16, May")
    expect(getColumnHeaders()[15].textContent).to.eq("17, May")
    expect(getColumnHeaders()[16].textContent).to.eq("18 - 19 May")
  })

  it("Shows an absence on a given day", () => {
    const absenceOnFri = AbsenceFactory.build({
      id: 1,
      startDate: moment().set({year: 2019, month: 4, date: 17}).startOf("day").toDate(),
      endDate: moment().set({year: 2019, month: 4, date: 17}).startOf("day").toDate(),
      reason: "Friday Absence"
    })
    const friMaintainer = MaintainerFactory.build({id: 1, absences: [absenceOnFri]})

    rendered = render(
      <MemoryRouter>
        <MaintainerList maintainers={[friMaintainer]}
                        date={moment().set({year: 2019, month: 4, date: 15})}
                        reloadMaintainer={() => null}/>
      </MemoryRouter>
    )
    container = rendered.container

    expect(getMaintainerFields(0)[15].textContent).to.include("Friday Absence")
  })

  it("Shows an absence for the weekend if the maintainer is out on either Sat or Sun", () => {
    const absenceOnSat = AbsenceFactory.build({
      id: 1,
      startDate: moment().set({year: 2019, month: 4, date: 18}).startOf("day").toDate(),
      endDate: moment().set({year: 2019, month: 4, date: 18}).startOf("day").toDate(),
      reason: "Saturday Absence"
    })
    const absenceOnSun = AbsenceFactory.build({
      id: 1,
      startDate: moment().set({year: 2019, month: 4, date: 19}).startOf("day").toDate(),
      endDate: moment().set({year: 2019, month: 4, date: 19}).startOf("day").toDate(),
      reason: "Sunday Absence"
    })

    const satMaintainer = MaintainerFactory.build({id: 1, absences: [absenceOnSat]})
    const sunMaintainer = MaintainerFactory.build({id: 2, absences: [absenceOnSun]})

    rendered = render(
      <MemoryRouter>
        <MaintainerList maintainers={[satMaintainer, sunMaintainer]}
                        date={moment().set({year: 2019, month: 4, date: 15})}
                        reloadMaintainer={() => null}/>
      </MemoryRouter>
    )
    container = rendered.container

    expect(getMaintainerFields(0)[16].textContent).to.equal("Saturday Absence")
    expect(getMaintainerFields(1)[16].textContent).to.equal("Sunday Absence")

    expect(container.querySelectorAll(".standby-button").length).to.equal(0)
  })

  it("Sorts maintainers by shift (ascending and descending)", async () => {

    const dayMaintainer = MaintainerFactory.build({shift: new Shift("D")})
    const midMaintainer = MaintainerFactory.build({shift: new Shift("M")})
    const swingMaintainer = MaintainerFactory.build({shift: new Shift("S")})

    rendered = render(
      <MemoryRouter>
        <MaintainerList maintainers={[swingMaintainer, midMaintainer, dayMaintainer]}
                        date={moment().set({year: 2019, month: 4, date: 15})}
                        reloadMaintainer={() => null}/>
      </MemoryRouter>
    )
    container = rendered.container

    expect(getMaintainerFields(0)[2].textContent).to.equal("Day")
    expect(getMaintainerFields(1)[2].textContent).to.equal("Swing")
    expect(getMaintainerFields(2)[2].textContent).to.equal("Mid")

    await fireEvent.click(container.querySelector("#header-Shift")!)
    await forIt()

    expect(getMaintainerFields(0)[2].textContent).to.equal("Mid")
    expect(getMaintainerFields(1)[2].textContent).to.equal("Swing")
    expect(getMaintainerFields(2)[2].textContent).to.equal("Day")

    await fireEvent.click(container.querySelector("#header-Shift")!)
    await forIt()

    expect(getMaintainerFields(0)[2].textContent).to.equal("Day")
    expect(getMaintainerFields(1)[2].textContent).to.equal("Swing")
    expect(getMaintainerFields(2)[2].textContent).to.equal("Mid")
  })

  it("Sorts maintainers by weekend standby", async () => {
    const standbyMaintainer = MaintainerFactory.build({lastName: "Standby", standbys: [new Standby(1,
          moment().set({year: 2029, month: 4, date: 19}).startOf("day").toDate(),
          moment().set({year: 2029, month: 4, date: 20}).startOf("day").toDate()
        )]})
    const otherMaintainer = MaintainerFactory.build({lastName: "Other"})

    rendered = render(
      <MemoryRouter>
        <MaintainerList maintainers={[otherMaintainer, standbyMaintainer]}
                        date={moment().set({year: 2029, month: 4, date: 19})}
                        reloadMaintainer={() => null}/>
      </MemoryRouter>
    )
    container = rendered.container

    await fireEvent.click(container.querySelector("#header-19-20May")!)
    await forIt()

    expect(getMaintainerFields(0)[0].textContent).to.contain("Standby")
    expect(getMaintainerFields(1)[0].textContent).to.contain("Other")
  })

  describe("A maintainer not on standby and not absent", () => {
    let reloadStub: sinon.SinonStub
    const postStub = sinon.stub(RequestHelper, "postJson")
      .withArgs("/api/maintainer/standby", sinon.match.any)
      .returns(Promise.resolve(false))

    beforeEach(() => {
      reloadStub = sinon.stub()

      const maintainer = MaintainerFactory.build()
      rendered = render(
        <MemoryRouter>
          <MaintainerList maintainers={[maintainer]}
                          date={moment().set({year: 2019, month: 4, date: 15})}
                          reloadMaintainer={reloadStub}/>
        </MemoryRouter>
      )
      container = rendered.container
    })

    afterEach(() => {
      postStub.reset()
    })

    it("Shows an 'ADD STANDBY' button if the maintainer is not absent and not on standby", () => {
      expect(container.querySelector(".standby-add-button")!.textContent).to.equal("Add Standby")
    })

    it("Clicking 'ADD STANDBY' results in a maintainer reload", async () => {
      await fireEvent.click(container.querySelector(".standby-add-button")!)
      await forIt()

      expect(reloadStub.callCount).to.eq(1)
    })
  })

  describe("A maintainer on standby", () => {
    let reloadStub: sinon.SinonStub
    const deleteStub = sinon.stub(RequestHelper, "delete")
      .withArgs("/api/maintainer/standby/1")
      .returns(Promise.resolve(false))

    beforeEach(() => {
      reloadStub = sinon.stub()

      const standby = new Standby(1, moment().set({year: 2019, month: 4, date: 18}).toDate(),
        moment().set({year: 2019, month: 4, date: 19}).toDate())
      const maintainer = MaintainerFactory.build({standbys: [standby]})
      rendered = render(
        <MemoryRouter>
          <MaintainerList maintainers={[maintainer]}
                          date={moment().set({year: 2019, month: 4, date: 15})}
                          reloadMaintainer={reloadStub}/>
        </MemoryRouter>
      )
      container = rendered.container
    })

    afterEach(() => {
      deleteStub.reset()
    })

    it("Shows 'STANDBY' if the maintainer has a standby", () => {
      expect(container.querySelector(".standby-delete-button")!.textContent).contains("Standby")
    })

    it("Clicking 'STANDBY' results in a maintainer reload", async () => {
      await fireEvent.click(container.querySelector(".standby-delete-button")!)
      await forIt()

      expect(reloadStub.callCount).to.eq(1)
    })
  })

  const getColumnHeaders = (): NodeListOf<HTMLTableDataCellElement> => (
    container.querySelectorAll("thead")[0].querySelectorAll("tr")[0]
      .querySelectorAll("th")
  )

  const getMaintainerFields = (index: number): NodeListOf<HTMLTableDataCellElement> => (
    container.querySelectorAll(".maintainer")[index].querySelectorAll("td, th")
  )
})
