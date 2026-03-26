import { useState } from "react"
import axios from "../services/api"
import { useNavigate, Link } from "react-router-dom"

function Register() {

  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [branch, setBranch] = useState("")
  const [cgpa, setCgpa] = useState("")

  const navigate = useNavigate()

  const register = async () => {

    if (!name || !email || !password || !branch || !cgpa) {
      alert("Please fill all fields")
      return
    }

    try {

      const response = await axios.post("/auth/register", {
        name,
        email,
        password,
        branch,
        cgpa
      })

      if (response.data.message) {
        alert(response.data.message)
        return
      }

      alert("Registration Successful")

      localStorage.setItem("studentId", response.data.id)

      navigate("/profile")

    } catch (err) {

      alert("Something went wrong")

    }

  }

  return (
    <div className="container animate-fade-in">
      <div style={{ display: "flex", justifyContent: "flex-end", marginBottom: "10px" }}>
        <button className="admin-btn" onClick={() => navigate("/login")}>
          Admin Login
        </button>
      </div>

      <h2>Create Account</h2>
      <p style={{ marginBottom: '24px', color: 'var(--text-secondary)' }}>Join our platform to track your goals</p>

      <input
        placeholder="Full Name"
        onChange={(e) => setName(e.target.value)}
      />

      <input
        type="email"
        placeholder="Email Address"
        onChange={(e) => setEmail(e.target.value)}
      />

      <input
        type="password"
        placeholder="Password"
        onChange={(e) => setPassword(e.target.value)}
      />

      <input
        placeholder="Branch"
        onChange={(e) => setBranch(e.target.value)}
      />

      <input
        placeholder="CGPA"
        onChange={(e) => setCgpa(e.target.value)}
      />

      <button onClick={register}>Create Account</button>

      <p style={{ marginTop: '24px' }}>
        Already have an account? <Link to="/login">Sign In</Link>
      </p>
    </div>
  )

}

export default Register