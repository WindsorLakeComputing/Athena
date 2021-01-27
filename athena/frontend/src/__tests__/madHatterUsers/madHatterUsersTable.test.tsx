import React from "react"
import MadHatterUsersTable from "../../components/MadHatterUsers/MadHatterUsersTable"
import {expect, render, sinon} from "../../testHelpers/importsHelper"

describe("Mad Hatter Users Table", () => {
  const madHatterUsers = [{
      firstName: "Bob",
      lastName: "Dole",
      email: "bob.dole@us.gov",
      amu: "BOLT"
  }]

  it("displays the passed in users", async () => {
    const tableBody = render(<MadHatterUsersTable madHatterUsers={madHatterUsers}/>).container.querySelector("tbody")

    expect(tableBody!.textContent).contains(madHatterUsers[0].firstName)
    expect(tableBody!.textContent).contains(madHatterUsers[0].lastName)
    expect(tableBody!.textContent).contains(madHatterUsers[0].email)
  })
})
