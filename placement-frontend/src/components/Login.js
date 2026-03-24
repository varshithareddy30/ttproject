import { useState } from "react"
import axios from "../services/api"
import { useNavigate, Link } from "react-router-dom"

function Login() {

  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")

  const navigate = useNavigate()

  const login = async () => {

    if (!email || !password) {
      alert("Enter email and password")
      return
    }

    try {

      const response = await axios.post("/auth/login", {
        email,
        password
      })

      console.log("FULL LOGIN RESPONSE:", response.data)

      // 🔥 FIX HERE
      const studentId = response.data.id || response.data.student?.id

      if (!studentId) {
        alert("Login failed: No student ID returned")
        return
      }

      localStorage.setItem("studentId", studentId)
      localStorage.setItem("role", response.data.role)

      alert("Login Successful")

      if (response.data.role === "ADMIN") {
        navigate("/admin")
      }
      else {
        navigate("/dashboard")
      }

    } catch (err) {

      alert("Invalid email or password")

    }

  }

  return (

    <div className="container animate-fade-in">

      <h2>Welcome Back</h2>

      <p style={{ marginBottom: '24px', color: 'var(--text-secondary)' }}>
        Sign in to continue your preparation
      </p>

      <input
        placeholder="Email address"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />

      <button onClick={login}>
        Sign In
      </button>

      <p style={{ marginTop: '24px' }}>
        Don't have an account? <Link to="/">Register</Link>
      </p>

    </div>

  )

}

export default Login