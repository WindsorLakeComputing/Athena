import React from "react"
import PrintIcon from "../components/PrintIcon"
import {expect, fireEvent, render, sinon} from "../testHelpers/importsHelper"

describe("Printing", () => {
  it("Calls the window.print() function on click", () => {
    const printSpy = sinon.stub(window, "print")
    const {container} = render(<PrintIcon/>)
    fireEvent.click(container.querySelector("a")!)
    expect(printSpy).to.have.been.called
  })
})
