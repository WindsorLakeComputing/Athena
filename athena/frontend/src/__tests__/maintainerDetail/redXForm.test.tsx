import React from "react"
import {RedXForm} from "../../components/maintainerDetail/RedXForm"
import Certificate from "../../models/Certificate"
import {expect, fireEvent, render, sinon} from "../../testHelpers/importsHelper"

describe("RedXForm", () => {
  it("'Not Red X Qualified' is selected when maintainer has no X cert", () => {
    const {container} = render(
      <RedXForm certs={[]} onSave={() => null}/>
    )

    expect((container.querySelector("#no-red-x-radio") as HTMLInputElement).checked).to.be.true
    expect((container.querySelector("#red-x-radio") as HTMLInputElement).checked).to.be.false
    expect((container.querySelector("#limited-red-x-radio") as HTMLInputElement).checked).to.be.false
  })

  it("'Red X Qualified' is selected when maintainer has X cert", () => {
    const {container} = render(
      <RedXForm certs={[new Certificate("X")]} onSave={() => null}/>
    )

    expect((container.querySelector("#no-red-x-radio") as HTMLInputElement).checked).to.be.false
    expect((container.querySelector("#red-x-radio") as HTMLInputElement).checked).to.be.true
    expect((container.querySelector("#limited-red-x-radio") as HTMLInputElement).checked).to.be.false
  })

  it("'Limited Red X Qualified' is selected when maintainer has L/X cert", () => {
    const {container} = render(
      <RedXForm certs={[new Certificate("L/X")]} onSave={() => null}/>
    )

    expect((container.querySelector("#no-red-x-radio") as HTMLInputElement).checked).to.be.false
    expect((container.querySelector("#red-x-radio") as HTMLInputElement).checked).to.be.false
    expect((container.querySelector("#limited-red-x-radio") as HTMLInputElement).checked).to.be.true
  })

  it("updates the visual status of the radio buttons on click", async () => {
    const {container} = render(
      <RedXForm certs={[]} onSave={() => null}/>
    )

    const redXRadio = container.querySelectorAll("#red-x-radio")[0]
    await fireEvent.click(redXRadio)

    expect((container.querySelector("#no-red-x-radio") as HTMLInputElement).checked).to.be.false
    expect((container.querySelector("#red-x-radio") as HTMLInputElement).checked).to.be.true
    expect((container.querySelector("#limited-red-x-radio") as HTMLInputElement).checked).to.be.false

    const limitedRedXRadio = container.querySelectorAll("#limited-red-x-radio")[0]
    await fireEvent.click(limitedRedXRadio)

    expect((container.querySelector("#no-red-x-radio") as HTMLInputElement).checked).to.be.false
    expect((container.querySelector("#red-x-radio") as HTMLInputElement).checked).to.be.false
    expect((container.querySelector("#limited-red-x-radio") as HTMLInputElement).checked).to.be.true

    const noRedXRadio = container.querySelectorAll("#no-red-x-radio")[0]
    await fireEvent.click(noRedXRadio)

    expect((container.querySelector("#no-red-x-radio") as HTMLInputElement).checked).to.be.true
    expect((container.querySelector("#red-x-radio") as HTMLInputElement).checked).to.be.false
    expect((container.querySelector("#limited-red-x-radio") as HTMLInputElement).checked).to.be.false
  })

  it("Calls the save function with 'X' cert after selecting red x", async () => {
    const saveSpy = sinon.stub()

    const {container} = render(
      <RedXForm certs={[]} onSave={saveSpy}/>
    )

    const redXRadio = container.querySelectorAll("#red-x-radio")[0]
    await fireEvent.click(redXRadio)

    expect(saveSpy).to.have.been.calledWith([new Certificate("X")])
  })

  it("Calls save without an 'X' cert after selecting No Red X button and clicking save", async () => {
    const saveSpy = sinon.stub()

    const {container} = render(
      <RedXForm certs={[new Certificate("unchanged"), new Certificate("X")]} onSave={saveSpy}/>
    )

    const noXRadio = container.querySelectorAll("#no-red-x-radio")[0]
    await fireEvent.click(noXRadio)

    expect(saveSpy).to.have.been.calledWith([new Certificate("unchanged")])
  })
})
