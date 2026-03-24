import { useState } from "react"
import axios from "../services/api"
import { useNavigate } from "react-router-dom"

function Profile() {

  const [skills, setSkills] = useState("")
  const [targetCompany, setTargetCompany] = useState("")
  const [role, setRole] = useState("")
  const [preferredDomain, setPreferredDomain] = useState("")

  const navigate = useNavigate()

  const studentId = localStorage.getItem("studentId") // ✅ correct

  const saveProfile = async () => {

    if (!skills || !targetCompany || !role || !preferredDomain) {
      alert("Fill all fields")
      return
    }

    try {

      await axios.put("/profile/update", {   // ✅ PUT (not POST)
        skills,
        targetCompany,
        role,
        preferredDomain,
        student: {
          id: studentId   // ✅ FIXED
        }
      })

      alert("Profile saved successfully")
      navigate("/domain")

    } catch (err) {
      console.error(err)
      alert("Error saving profile")
    }
  }

  return (

    <div className="container">

      <h2>Complete Your Profile</h2>

      <input
        placeholder="Skills (Java, DSA, etc.)"
        onChange={(e) => setSkills(e.target.value)}
      />

      <input
        placeholder="Target Company"
        onChange={(e) => setTargetCompany(e.target.value)}
      />

      <input
        placeholder="Preferred Role"
        onChange={(e) => setRole(e.target.value)}
      />

      <input
        placeholder="Preferred Domain"
        onChange={(e) => setPreferredDomain(e.target.value)}
      />

      <button onClick={saveProfile}>
        Save Profile
      </button>

    </div>
  )
}

export default Profile