import React from "react"
import {RenderResult} from "react-testing-library"
import GenerateGroup from "../../components/scheduleView/GenerateGroup"
import RequestHelper from "../../helpers/RequestHelper"
import { expect, fireEvent, render, sinon } from "../../testHelpers/importsHelper"
import {forIt} from "../../testHelpers/testHelpers"

let container: HTMLElement
let rendered: RenderResult

describe("GenerateGroup", () => {
  beforeEach(() => {
    rendered = render(<GenerateGroup/>)
    container = rendered.container
  })

  it("Displays Generate schedule button and success text on successful request" , async () => {
    const {getByText} = render(<GenerateGroup/>)
    const postStub = sinon.stub(RequestHelper, "post").returns(Promise.resolve(false))

    const generateButton = getByText("Generate Schedule")

    fireEvent.click(generateButton)
    expect(postStub.called).to.be.true
    await forIt()
    const successfulString = getByText("Successful Request")
    expect(successfulString).to.exist

    postStub.restore()
  })

  it("Displays Generate schedule button and error text on failed request" , async () => {
    const {getByText} = render(<GenerateGroup/>)
    const postStub = sinon.stub(RequestHelper, "post").returns(Promise.resolve(true))

    const generateButton = getByText("Generate Schedule")

    fireEvent.click(generateButton)
    expect(postStub.called).to.be.true
    await forIt()
    const errorString = getByText("Error Sending Request")
    expect(errorString).to.exist

    postStub.restore()
  })
})
