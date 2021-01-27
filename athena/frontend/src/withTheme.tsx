import CssBaseline from "@material-ui/core/CssBaseline"
import { createMuiTheme, MuiThemeProvider } from "@material-ui/core/styles"
import * as React from "react"

const theme = createMuiTheme({
  palette: {
    primary: {
      main: "#292D33"
    },
    secondary: {
      main: "#2c90ee"
    }
  },
  overrides: {
    MuiCheckbox: {
      root: {
        color: "#9e9e9e"
      }
    }
  },
  typography: {
    useNextVariants: true
  }
})

function withTheme<P>(Component: React.ComponentType<P>) {
  function WithTheme(props: P) {
    return (
      <MuiThemeProvider theme={theme}>
        <CssBaseline />
        <Component {...props} />
      </MuiThemeProvider>
    )
  }

  return WithTheme
}

export default withTheme
