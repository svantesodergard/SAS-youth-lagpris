import { useState, useEffect } from 'react'
import axios from 'axios'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

const api = "http://localhost:8080"


const Calendar = (departure) => {
    const [calendar, setCalendar] = useState("")

    useEffect(() => axios.get(`${api}/getAllMonths`).then(response => {
      setMonths(response.data)
    }), [])

    useEffect(() => { axios.get(`${api}/lowestPricesByMonth?startMonth=5&startYear=2025&departure=${departure.departure}&monthCount=6`).then(response => {
        setCalendar (
          <table>
            <td>
              <Column month={"-"} prices={Object.values(response.data)[0].map(price => <strong>{price.destination}</strong>)} />
            </td>
          {Object.entries(response.data).map( month => 
              <td>
                <Column month={<strong>{month[0]}</strong>} prices={Object.values(month[1]).map(price => <a href={price.bookingLink} target='_blank'>{price.lowestPrice}</a>)} />
              </td>
          )}
          </table>
        )
    })}, [departure])

    return calendar
}

const Column = ({month, prices}) => {
  return <>
    <tr><td>{month}</td></tr>
    { prices.map(price => <tr><td>{price}</td></tr>) }
  </>
}


function App() {
  const [departure, setDeparture] = useState("")
  const handleChange = e => setDeparture(e.target.value)
  const [airports, setAirports] = useState([])

  useEffect(() => {
    axios.get(`${api}/getAllAirports`).then(response => {
      setAirports(response.data)
      setDeparture(response.data[0])
    })}, [])

  return (
    <>
      <select style={{float: 'left'}} value={departure} onChange={handleChange}>
        {airports.map(airport => <option value={airport}>{airport}</option>)}
      </select>

      <br /><br />

      <Calendar departure={departure} />
  </>
  )
}

export default App
