import React, { useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { AppContext } from '../App'
import { customerAPI } from '../services/api'
import PageWrapper from '../components/PageWrapper'
import Button from '../components/Button'
import Input from '../components/Input'
import Alert from '../components/Alert'

const INITIAL = { firstName: '', lastName: '', email: '', dob: '', address: '', idProof: '' }

function validate(form) {
  const errs = {}
  if (!form.firstName.trim()) errs.firstName = 'First name is required'
  if (!form.lastName.trim()) errs.lastName = 'Last name is required'
  if (!form.email.trim()) errs.email = 'Email is required'
  else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) errs.email = 'Enter a valid email'
  if (!form.dob) errs.dob = 'Date of birth is required'
  else {
    const age = (new Date() - new Date(form.dob)) / (365.25 * 24 * 3600 * 1000)
    if (age < 18) errs.dob = 'Must be at least 18 years old'
  }
  if (!form.address.trim() || form.address.trim().length < 5) errs.address = 'Enter a complete address'
  if (!form.idProof.trim()) errs.idProof = 'ID proof is required'
  return errs
}

export default function CustomerVerificationPage() {
  const { flowData, updateFlowData } = useContext(AppContext)
  const navigate = useNavigate()

  const [form, setForm] = useState(INITIAL)
  const [errors, setErrors] = useState({})
  const [apiError, setApiError] = useState('')
  const [loading, setLoading] = useState(false)

  if (!flowData.sim) {
    navigate('/validate')
    return null
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
    if (errors[name]) setErrors(prev => ({ ...prev, [name]: '' }))
  }

  const handleSubmit = async () => {
    const errs = validate(form)
    if (Object.keys(errs).length) { setErrors(errs); return }
    setLoading(true)
    setApiError('')
    try {
      const res = await customerAPI.validate(form)
      updateFlowData('customer', res.data.data)
      navigate('/plans')
    } catch (err) {
      setApiError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const field = (name, label, type = 'text', placeholder, hint) => (
    <Input
      label={label}
      name={name}
      type={type}
      placeholder={placeholder}
      value={form[name]}
      onChange={handleChange}
      error={errors[name]}
      hint={hint}
      required
    />
  )

  return (
    <PageWrapper>
      <div style={styles.page}>
        <div style={styles.card} className="glass gradient-border">
          <div style={styles.header}>
            <div style={styles.iconWrap}><UserIcon /></div>
            <h2 style={styles.title}>Customer Verification</h2>
            <p style={styles.subtitle}>Please enter your details to proceed with SIM activation.</p>
          </div>

          <div style={styles.body}>
            {apiError && <Alert type="error" message={apiError} onClose={() => setApiError('')} />}

            <div style={styles.grid2}>
              {field('firstName', 'First Name', 'text', 'John')}
              {field('lastName',  'Last Name',  'text', 'Doe')}
            </div>

            {field('email', 'Email Address', 'email', 'john.doe@example.com')}

            {field('dob', 'Date of Birth', 'date', '', 'Must be 18+ years old')}

            {field('address', 'Address', 'text', '12 Main Street, City, State - 600001')}

            <div style={styles.group}>
              <label style={styles.label}>ID Proof Type <span style={{ color: 'var(--color-error)' }}>*</span></label>
              <select
                name="idProof"
                value={form.idProof}
                onChange={handleChange}
                style={{
                  ...styles.select,
                  borderColor: errors.idProof ? 'var(--color-error)' : 'var(--color-border)',
                }}
              >
                <option value="">Select ID proof</option>
                <option value="AADHAR">Aadhaar Card</option>
                <option value="PAN">PAN Card</option>
                <option value="PASSPORT">Passport</option>
                <option value="DRIVING_LICENSE">Driving License</option>
                <option value="VOTER_ID">Voter ID</option>
              </select>
              {errors.idProof && <p style={styles.err}>{errors.idProof}</p>}
            </div>

            <div style={styles.actions}>
              <Button variant="ghost" onClick={() => navigate('/validate')}>← Back</Button>
              <Button loading={loading} onClick={handleSubmit} icon={<ArrowIcon />}>
                Continue to Plans
              </Button>
            </div>
          </div>
        </div>
      </div>
    </PageWrapper>
  )
}

function UserIcon() {
  return <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="1.8"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
}
function ArrowIcon() {
  return <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
}

const styles = {
  page: {
    minHeight: 'calc(100vh - 128px)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    padding: '48px 24px',
  },
  card: { width: '100%', maxWidth: 580 },
  header: {
    padding: '36px 36px 28px',
    borderBottom: '1px solid var(--color-border)',
    textAlign: 'center',
  },
  iconWrap: {
    width: 60, height: 60, borderRadius: 16,
    background: 'linear-gradient(135deg, #3B82F6, #06b6d4)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    margin: '0 auto 20px',
    boxShadow: '0 0 24px rgba(59,130,246,0.35)',
  },
  title: {
    fontFamily: 'var(--font-display)', fontSize: 24, fontWeight: 700,
    color: 'var(--color-text)', marginBottom: 10, letterSpacing: '-0.5px',
  },
  subtitle: { fontSize: 14, color: 'var(--color-text-muted)', lineHeight: 1.6 },
  body: {
    padding: '28px 36px 36px',
    display: 'flex', flexDirection: 'column', gap: 18,
  },
  grid2: {
    display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16,
  },
  group: { display: 'flex', flexDirection: 'column', gap: 6 },
  label: {
    fontSize: 13, fontWeight: 600, color: 'var(--color-text-muted)',
    fontFamily: 'var(--font-display)', letterSpacing: '0.2px',
  },
  select: {
    padding: '13px 16px',
    background: 'rgba(255,255,255,0.04)',
    border: '1px solid var(--color-border)',
    borderRadius: 12, color: 'var(--color-text)',
    fontSize: 15, fontFamily: 'var(--font-body)',
    outline: 'none', cursor: 'pointer',
    appearance: 'none',
    backgroundImage: `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 24 24' fill='none' stroke='%238ba3c7' stroke-width='2'%3E%3Cpolyline points='6 9 12 15 18 9'/%3E%3C/svg%3E")`,
    backgroundRepeat: 'no-repeat',
    backgroundPosition: 'right 14px center',
  },
  err: { fontSize: 12, color: 'var(--color-error)', fontFamily: 'var(--font-body)' },
  actions: {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    paddingTop: 8,
  },
}
