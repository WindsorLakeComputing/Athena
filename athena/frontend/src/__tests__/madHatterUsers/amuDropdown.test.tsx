import React from "react"
import MadHatterUserForm from "../../components/MadHatterUsers/MadHatterUserForm"
import {selectFromDropdown} from "../../helpers/ComponentHelper"
import {expect, fireEvent, render} from "../../testHelpers/importsHelper"

describe("AMU Dropdown", () => {
  it("Displays the selected AMU option in the dropdown", async () => {
    const container = render(
      <MadHatterUserForm madHatterUsers={[]}
                         createMadHatterUser={() => Promise.resolve(true)}/>).container
    const select = container.querySelectorAll("#amu-dropdown")[0]
    await selectFromDropdown(select, "Bolt")

    expect(select.querySelectorAll(".react-select__single-value")[0].textContent).to.contain("Bolt")
    expect(container.textContent).to.not.contain("please complete")
  })

  it("Displays an error message if nothing is selected on blur", async () => {
    const container = render(
      <MadHatterUserForm madHatterUsers={[]}
                         createMadHatterUser={() => Promise.resolve(true)}/>).container

    await fireEvent.blur(container.querySelectorAll("#amu-dropdown input")[0])

    expect(container.textContent).to.contain("please complete")
  })

  it("Error message goes away after picking an option", async () => {
    const container = render(
      <MadHatterUserForm madHatterUsers={[]}
                         createMadHatterUser={() => Promise.resolve(true)}/>).container

    await fireEvent.blur(container.querySelectorAll("#amu-dropdown input")[0])
    const select = container.querySelectorAll("#amu-dropdown")[0]
    await selectFromDropdown(select, "Bolt")

    expect(container.textContent).to.not.contain("please complete")
  })
})
