import React, { useState } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { AnimatePresence } from 'framer-motion'
import Navbar from './components/Navbar'
import Footer from './components/Footer'
import LandingPage from './pages/LandingPage'
import SimValidationPage from './pages/SimValidationPage'
import CustomerVerificationPage from './pages/CustomerVerificationPage'
import PlanSelectionPage from './pages/PlanSelectionPage'
import AddressPage from './pages/AddressPage'
import SuccessPage from './pages/SuccessPage'

export const AppContext = React.createContext(null)

const STEPS = [
  { path: '/',          label: 'Start'    },
  { path: '/validate',  label: 'SIM'      },
  { path: '/verify',    label: 'Details'  },
  { path: '/plans',     label: 'Plan'     },
  { path: '/address',   label: 'Address'  },
  { path: '/success',   label: 'Done'     },
]

export default function App() {
  const [flowData, setFlowData] = useState({
    sim: null,
    customer: null,
    offer: null,
  })

  const updateFlowData = (key, value) =>
    setFlowData(prev => ({ ...prev, [key]: value }))

  return (
    <AppContext.Provider value={{ flowData, updateFlowData, STEPS }}>
      <div style={{ position: 'relative', zIndex: 1, minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
        <Navbar />
        <main style={{ flex: 1 }}>
          <AnimatePresence mode="wait">
            <Routes>
              <Route path="/"         element={<LandingPage />} />
              <Route path="/validate" element={<SimValidationPage />} />
              <Route path="/verify"   element={<CustomerVerificationPage />} />
              <Route path="/plans"    element={<PlanSelectionPage />} />
              <Route path="/address"  element={<AddressPage />} />
              <Route path="/success"  element={<SuccessPage />} />
              <Route path="*"         element={<Navigate to="/" replace />} />
            </Routes>
          </AnimatePresence>
        </main>
        <Footer />
      </div>
    </AppContext.Provider>
  )
}
