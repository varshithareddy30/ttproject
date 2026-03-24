import { useState } from "react"
import axios from "../services/api"
import { useNavigate } from "react-router-dom"

function Domain(){

const [domains,setDomains] = useState([])

const navigate = useNavigate()

const studentId = localStorage.getItem("studentId")

const handleChange = (e) => {

const value = e.target.value

if(e.target.checked){
setDomains([...domains,value])
}
else{
setDomains(domains.filter(d => d !== value))
}

}

const saveDomains = async () => {

if(domains.length === 0){
alert("Select at least one domain")
return
}

try{

for(let d of domains){

await axios.post("/domain/select",{
name:d,
student:{
id:studentId
}
})

}

alert("Domains saved")

navigate("/login")

}catch(err){

alert("Error saving domains")

}

}

  return(
    <div className="container animate-fade-in">
      <h2>Select Domains</h2>
      <p style={{marginBottom: '24px', color: 'var(--text-secondary)'}}>Choose areas you want to prepare for</p>

      <div style={{display: 'flex', flexDirection: 'column', gap: '8px'}}>
        <label className="selectable-card">
          <input type="checkbox" value="Coding" onChange={handleChange}/>
          Coding & Algorithms
        </label>
        
        <label className="selectable-card">
          <input type="checkbox" value="Aptitude" onChange={handleChange}/>
          Quantitative Aptitude
        </label>
        
        <label className="selectable-card">
          <input type="checkbox" value="Core" onChange={handleChange}/>
          Core Subjects (OS, DBMS, CN)
        </label>
        
        <label className="selectable-card">
          <input type="checkbox" value="Communication" onChange={handleChange}/>
          Communication & HR
        </label>
      </div>

      <button onClick={saveDomains} style={{marginTop: '24px'}}>
        Save & Continue
      </button>
    </div>
  )

}

export default Domain