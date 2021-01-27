# Tech Choices
 
## Athena
### Backend 
#### Kotlin
1. Be in alignment Mad Hatter teams on backend technologies
    1. Sharing solutions between the teams
    1. Be confident all MH apps could integrate easily
1. Security team supported language
1. Opportunty to leverage the strengths of modern languages
    1. Immutable value objects
    1. lambda expressions are more usable than alteratives
    1. built in null-safety
1. Similar to our frontend language choice, Typescript, which provides a consistent development experience
    
#### JUnit 4 
1. it was used by default and able to meet our testing needs

#### Spring Boot
1. Facilitates path to prod based on pipelines already used by the KRES security team 
1. Lets us quickly and easily deploy to PCF

#### Data Persistence
1. We are using Crunchy.
    1. We are storing some json has strings currently (??)
1. We are using JPA/Hibernate
    
### Frontend    
#### React
1. The team wants to make sure it can use the Sawzall style-guide
    1. Unsure if we want to use Sawzall as a component library
    1. the components that make Sawzall a large bundle are not currently needed in Athena's design
1. Well supported, easy to google most problems
1. React is being taught at the coding bootcamp for new engineers

#### Cypress
1. Test runner UI is great
1. Straightforward syntax
1. Current stories are oriented towards basic utility and API integrations, so we need UI testing more than testing user flows and functionality of components.

#### Typescript
1. The team decided to use typescript because of its ability to catch bugs and improve code quality. 

### Database
#### Crunchy
1. can have machine learning systems installed on the database to support our data science efforts
1. optimizes queries for us so we can focus on other aspects of the project
1. fastest option on the current KR supported databases list


## Minerva
### Python
1. has an available buildpack on PCF that allows us to vendor needed dependencies
1. supports data science solvers and is known widely in the data science community

### Flask
1. deploys easiest to PCF

### ??? whatever the worker thing is called 
