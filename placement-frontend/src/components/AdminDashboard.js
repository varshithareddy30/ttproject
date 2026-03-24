import { useEffect, useState } from "react"
import axios from "../services/api"
import "../Admin.css"
import { useNavigate } from "react-router-dom"

function AdminDashboard() {

    const [users, setUsers] = useState([])
    const [selectedGoals, setSelectedGoals] = useState([])
    const [selectedUser, setSelectedUser] = useState(null)

    // ✅ NEW STATES
    const [showForm, setShowForm] = useState(false)
    const [title, setTitle] = useState("")
    const [type, setType] = useState("")
    const [target, setTarget] = useState("")

    const navigate = useNavigate()

    const handleLogout = () => {
        localStorage.removeItem("studentId")
        localStorage.removeItem("role")
        navigate("/login")
    }

    useEffect(() => {
        loadUsers()
    }, [])

    const loadUsers = async () => {
        try {
            const res = await axios.get("/admin/users")
            setUsers(res.data)
        } catch (err) {
            console.error("Error loading users", err)
        }
    }

    const viewGoals = async (userId, name) => {
        try {
            const res = await axios.get(`/admin/goals/${userId}`)
            setSelectedGoals(res.data)
            setSelectedUser(name)
        } catch (err) {
            console.error("Error loading goals", err)
        }
    }

    // ✅ CREATE GLOBAL GOAL
    const createGlobalGoal = async () => {

        if (!title || !type || !target) {
            alert("Fill all fields")
            return
        }

        try {

            await axios.post("/admin/createGlobalGoal", {
                title,
                type,
                target: Number(target),
                completed: 0
            })

            alert("Global Goal Created ✅")

            setShowForm(false)
            setTitle("")
            setType("")
            setTarget("")

        } catch (err) {
            console.error(err)
            alert("Error creating goal")
        }
    }

    return (

        <div className="adminPage">

            {/* HEADER */}
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <h1 className="adminTitle">Admin Dashboard</h1>

                <div style={{ display: "flex", gap: "10px" }}>

                    {/* ✅ NEW BUTTON */}
                    <button className="add-goal-btn" onClick={() => setShowForm(!showForm)}>
                        {showForm ? "Close" : "+ Global Goal"}
                    </button>

                    <button className="logout-btn" onClick={handleLogout}>
                        Logout
                    </button>

                </div>
            </div>

            {/* ✅ GLOBAL GOAL FORM */}
            {showForm && (
                <div className="goalCard" style={{ margin: "20px" }}>

                    <h2>Create Global Goal</h2>

                    <input
                        placeholder="Goal Title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />

                    <select value={type} onChange={(e) => setType(e.target.value)}>
                        <option value="">Select Type</option>
                        <option value="mcq">MCQ</option>
                        <option value="coding">Coding</option>
                    </select>

                    <input
                        type="number"
                        placeholder="Target"
                        value={target}
                        onChange={(e) => setTarget(e.target.value)}
                    />

                    <button onClick={createGlobalGoal}>
                        Create Goal
                    </button>

                </div>
            )}

            <div className="adminContainer">

                {/* USERS */}
                <div className="userPanel">

                    <h2>Users</h2>

                    {users.length === 0 && <p>No users found</p>}

                    {users.map(u => (

                        <div key={u.id} className="userCard">

                            <div>
                                <b>{u.name}</b>
                                <p>{u.email}</p>
                            </div>

                            <div className="adminBtns">

                                <button
                                    className="viewBtn"
                                    onClick={() => viewGoals(u.id, u.name)}
                                >
                                    View Goals
                                </button>

                            </div>

                        </div>

                    ))}

                </div>

                {/* GOALS */}
                <div className="goalPanel">

                    <h2>
                        {selectedUser ? `${selectedUser}'s Goals` : "User Goals"}
                    </h2>

                    {selectedGoals.length === 0 && (
                        <p>No goals selected</p>
                    )}

                    {selectedGoals.map(g => (

                        <div key={g.id} className="goalCard">

                            <b>{g.title}</b>

                            {/* ✅ SHOW GLOBAL TAG */}
                            {g.globalGoal && (
                                <span style={{ color: "#8b5cf6", marginLeft: "10px" }}>
                                    GLOBAL
                                </span>
                            )}

                            <p>Type: {g.type}</p>
                            <p>Target: {g.target}</p>
                            <p>Completed: {g.completed}</p>

                        </div>

                    ))}

                </div>

            </div>

        </div>

    )
}

export default AdminDashboard