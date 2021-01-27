import * as React from "react"
import sinon from "sinon"
import {MaintainerAbsenceRow} from "../../components/maintainerDetail/MaintainerAbsenceRow"
import {AbsenceFactory} from "../../factories/AbsenceFactory"
import {render} from "../../testHelpers/importsHelper"

describe("MaintainerAbsenceRow", () => {
  it("Displays 'Entire Shift' for the Timing column when hours is empty", () => {
    const deleteAbsenceSpy = sinon.spy()

    const absence = AbsenceFactory.build()
    const {container} = render(
      <table>
        <tbody>
        <MaintainerAbsenceRow absence={absence} deleteAbsence={deleteAbsenceSpy}/>
        </tbody>
      </table>
    )

    expect(container.querySelectorAll("td")[2].textContent).toEqual("Entire Shift")
  })

  it("Displays absence hours when hours are defined", () => {
    const deleteAbsenceSpy = sinon.spy()

    const absenceHours = "1200-1345"
    const absence = AbsenceFactory.build({hours: absenceHours})

    const {container} = render(
      <table>
        <tbody>
        <MaintainerAbsenceRow absence={absence} deleteAbsence={deleteAbsenceSpy}/>
        </tbody>
      </table>
    )

    expect(container.querySelectorAll("td")[2].textContent).toEqual(absenceHours)
  })
})
