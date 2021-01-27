import chai, {expect as Expect} from "chai"
import { render as Render } from "react-testing-library"
import { fireEvent as FireEvent } from "react-testing-library"
import { wait as Wait } from "react-testing-library"
import * as Sinon from "sinon"
import sinonChai from "sinon-chai"
chai.use(sinonChai)

export const expect = Expect
export const sinon = Sinon
export const render = Render
export const fireEvent = FireEvent
export const wait = Wait
