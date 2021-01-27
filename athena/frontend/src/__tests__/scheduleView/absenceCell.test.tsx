import moment from "moment"
import * as React from "react"
import {render} from "react-testing-library"
import AbsenceCell from "../../components/scheduleView/AbsenceCell"
import {AbsenceFactory} from "../../factories/AbsenceFactory"
import {expect} from "../../testHelpers/importsHelper"

describe("Absence Cell", () => {
  it("displays the number of absences in a day for a maintainer", () => {
    const date = moment()

    const absences = [
      AbsenceFactory.build({
        startDate: date.toDate(),
        endDate: date.toDate()
      }),
      AbsenceFactory.build({
        startDate: date.toDate(),
        endDate: date.toDate()
      })
    ]
    const {container} = renderInTable(<AbsenceCell absences={absences}/>)

    expect(container.textContent).to.equal("2 Events")
  })

  it("Shows the reason and the hours for an absence on a given day", () => {
    const absenceOnSat = AbsenceFactory.build({
      id: 1,
      startDate: moment().set({year: 2019, month: 4, date: 17}).startOf("day").toDate(),
      endDate: moment().set({year: 2019, month: 4, date: 17}).startOf("day").toDate(),
      reason: "Friday Absence",
      hours: "1100-1200"
    })

    const {container} = renderInTable(<AbsenceCell absences={[absenceOnSat]}/>)

    expect(container.textContent).to.include("Friday Absence")
    expect(container.textContent).to.include("1100-1200")
  })

  it("Doesn't show anything if there's no absence", () => {
    const {container} = renderInTable(<AbsenceCell absences={[]}/>)

    expect(container.textContent).to.equal("")
  })
})

const renderInTable = (element: JSX.Element) => render(
  <table>
    <tbody>
      <tr>
        {element}
      </tr>
    </tbody>
  </table>
)
