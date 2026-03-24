import { useState } from "react"
import axios from "../services/api"
import { useNavigate } from "react-router-dom"

function Goal() {

  const [title, setTitle] = useState("")
  const [type, setType] = useState("")
  const [target, setTarget] = useState("")
  const [difficulty, setDifficulty] = useState("")

  const navigate = useNavigate()

  const createGoal = async () => {

    const studentId = Number(localStorage.getItem("studentId"))

    if (!studentId) {
      alert("User not logged in properly")
      return
    }

    if (!title || !type || !target || !difficulty) {
      alert("Please fill all fields")
      return
    }

    const payload = {
      title,
      type,
      target: Number(target),
      difficulty,
      student: {
        id: studentId
      }
    }

    try {
      await axios.post("/goal/create", payload)

      alert("Goal Created Successfully")
      navigate("/dashboard")

    } catch (err) {

      console.error(err)

      if (err.response) {
        alert(err.response.data.message)
      } else {
        alert("Server not reachable")
      }
    }
  }

  return (

    <div className="goalPage">

      <div className="goalCard">

        <h2>Create Goal</h2>

        <input
          placeholder="Goal Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        <select value={type} onChange={(e) => setType(e.target.value)}>
          <option value="">Select Test Type</option>
          <option value="coding">Coding</option>
          <option value="mcq">MCQs</option>
          <option value="mixed">Mixed</option>
        </select>

        <select value={difficulty} onChange={(e) => setDifficulty(e.target.value)}>
          <option value="">Select Difficulty</option>
          <option value="easy">Easy</option>
          <option value="medium">Medium</option>
          <option value="hard">Hard</option>
        </select>

        <input
          type="number"
          placeholder="Target"
          value={target}
          onChange={(e) => setTarget(e.target.value)}
        />

        <button onClick={createGoal}>
          Create Goal
        </button>

      </div>

    </div>
  )
}

export default Goal