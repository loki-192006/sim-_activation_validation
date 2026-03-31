import React, { useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { AppContext } from '../App'
import { customerAPI, simAPI } from '../services/api'
import PageWrapper from '../components/PageWrapper'
import Button from '../components/Button'
import Input from '../components/Input'
import Alert from '../components/Alert'

export default function AddressPage() {
  const { flowData, updateFlowData } = useContext(AppContext)
  const navigate = useNavigate()

  const [address, setAddress] = useState(flowData.customer?.address || '')
  const [error, setError] = useState('')
  const [apiError, setApiError] = useState('')
  const [loading, setLoading] = useState(false)

  if (!flowData.offer) { navigate('/plans'); return null }

  const handleSubmit = async () => {
    if (!address.trim() || address.trim().length < 5) {
      setError('Please enter a valid complete address.')
      return
    }
    setLoading(true)
    setApiError('')
    try {
      // 1. Update address
      await customerAPI.updateAddress({
        customerId: flowData.customer.customerId,
        address: address.trim(),
      })

      // 2. Activate SIM
      const res = await simAPI.activate({
        simNumber: flowData.sim.simNumber,
        customerId: flowData.customer.customerId,
        offerId: flowData.offer.offerId,
      })

      updateFlowData('activation', res.data.data)
      navigate('/success')
    } catch (err) {
      setApiError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const { customer, sim, offer } = flowData

  return (
    <PageWrapper>
      <div style={styles.page}>
        <div style={styles.layout}>
          {/* Main form */}
          <div style={styles.card} className="glass gradient-border">
            <div style={styles.header}>
              <div style={styles.iconWrap}><MapIcon /></div>
              <h2 style={styles.title}>Confirm Your Address</h2>
              <p style={styles.subtitle}>
                This address will be used for correspondence and SIM delivery if required.
              </p>
            </div>

            <div style={styles.body}>
              {apiError && <Alert type="error" message={apiError} onClose={() => setApiError('')} />}

              <div style={styles.addrGroup}>
                <label style={styles.label}>
                  Delivery / Correspondence Address <span style={{ color: 'var(--color-error)' }}>*</span>
                </label>
                <textarea
                  value={address}
                  onChange={e => { setAddress(e.target.value); setError('') }}
                  placeholder="House No, Street, Area, City, State - PIN Code"
                  rows={4}
                  style={{
                    ...styles.textarea,
                    borderColor: error ? 'var(--color-error)' : 'var(--color-border)',
                  }}
                />
                {error && <p style={styles.err}>{error}</p>}
                <p style={styles.hint}>{address.trim().length} characters</p>
              </div>

              <div style={styles.actions}>
                <Button variant="ghost" onClick={() => navigate('/plans')}>← Back</Button>
                <Button loading={loading} onClick={handleSubmit} size="lg" icon={<RocketIcon />}>
                  {loading ? 'Activating SIM…' : 'Activate SIM'}
                </Button>
              </div>
            </div>
          </div>

          {/* Order summary */}
          <div style={styles.summary} className="glass">
            <h3 style={styles.summaryTitle}>Activation Summary</h3>

            <div style={styles.summarySection}>
              <p style={styles.sectionLabel}>SIM Card</p>
              <p style={styles.sectionValue}>{sim?.simNumber}</p>
            </div>

            <div style={styles.divider} />

            <div style={styles.summarySection}>
              <p style={styles.sectionLabel}>Customer</p>
              <p style={styles.sectionValue}>{customer?.firstName} {customer?.lastName}</p>
              <p style={styles.sectionSub}>{customer?.email}</p>
            </div>

            <div style={styles.divider} />

            <div style={styles.summarySection}>
              <p style={styles.sectionLabel}>Selected Plan</p>
              <div style={styles.planCard}>
                <div style={styles.planInfo}>
                  <p style={styles.planName}>{offer?.planName}</p>
                  <p style={styles.planValidity}>{offer?.validity}</p>
                </div>
                <p style={styles.planPrice}>₹{offer?.price}</p>
              </div>
              <div style={styles.planFeatures}>
                <span style={styles.planFeature}>📶 {offer?.dataLimit}</span>
                <span style={styles.planFeature}>📞 {offer?.calls}</span>
                <span style={styles.planFeature}>💬 {offer?.sms}</span>
              </div>
            </div>

            <div style={styles.totalRow}>
              <span style={styles.totalLabel}>Total Amount</span>
              <span style={styles.totalAmount}>₹{offer?.price}</span>
            </div>

            <p style={styles.disclaimer}>
              * Activation is instant. No physical delivery needed for digital SIMs.
            </p>
          </div>
        </div>
      </div>
    </PageWrapper>
  )
}

function MapIcon() {
  return <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="1.8"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>
}
function RocketIcon() {
  return <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M4.5 16.5c-1.5 1.26-2 5-2 5s3.74-.5 5-2c.71-.84.7-2.13-.09-2.91a2.18 2.18 0 0 0-2.91-.09z"/><path d="m12 15-3-3a22 22 0 0 1 2-3.95A12.88 12.88 0 0 1 22 2c0 2.72-.78 7.5-6 11a22.35 22.35 0 0 1-4 2z"/><path d="M9 12H4s.55-3.03 2-4c1.62-1.08 5 0 5 0"/><path d="M12 15v5s3.03-.55 4-2c1.08-1.62 0-5 0-5"/></svg>
}

const styles = {
  page: { padding: '48px 24px 80px', maxWidth: 1000, margin: '0 auto' },
  layout: { display: 'grid', gridTemplateColumns: '1fr 360px', gap: 28, alignItems: 'start', flexWrap: 'wrap' },
  card: { width: '100%' },
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
  body: { padding: '28px 36px 36px', display: 'flex', flexDirection: 'column', gap: 20 },
  addrGroup: { display: 'flex', flexDirection: 'column', gap: 8 },
  label: {
    fontSize: 13, fontWeight: 600, color: 'var(--color-text-muted)',
    fontFamily: 'var(--font-display)', letterSpacing: '0.2px',
  },
  textarea: {
    width: '100%', padding: '14px 16px',
    background: 'rgba(255,255,255,0.04)',
    border: '1px solid',
    borderRadius: 12,
    color: 'var(--color-text)', fontSize: 15,
    fontFamily: 'var(--font-body)', lineHeight: 1.6,
    outline: 'none', resize: 'vertical',
    transition: 'border-color 0.2s',
  },
  err: { fontSize: 12, color: 'var(--color-error)' },
  hint: { fontSize: 12, color: 'var(--color-text-dim)', textAlign: 'right' },
  actions: {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
  },
  summary: { padding: 28, borderRadius: 20 },
  summaryTitle: {
    fontFamily: 'var(--font-display)', fontSize: 16, fontWeight: 700,
    color: 'var(--color-text)', marginBottom: 24, letterSpacing: '-0.3px',
  },
  summarySection: { marginBottom: 16 },
  sectionLabel: {
    fontSize: 11, color: 'var(--color-text-dim)',
    textTransform: 'uppercase', letterSpacing: '1px',
    fontFamily: 'var(--font-display)', marginBottom: 6,
  },
  sectionValue: {
    fontSize: 14, fontWeight: 600, color: 'var(--color-text)',
    fontFamily: 'var(--font-body)',
  },
  sectionSub: { fontSize: 13, color: 'var(--color-text-muted)' },
  divider: { height: 1, background: 'var(--color-border)', margin: '16px 0' },
  planCard: {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    marginBottom: 10,
  },
  planInfo: {},
  planName: {
    fontSize: 15, fontWeight: 700, color: 'var(--color-text)',
    fontFamily: 'var(--font-display)',
  },
  planValidity: { fontSize: 12, color: 'var(--color-text-muted)' },
  planPrice: {
    fontSize: 20, fontWeight: 800, color: 'var(--color-primary)',
    fontFamily: 'var(--font-display)',
  },
  planFeatures: { display: 'flex', flexDirection: 'column', gap: 4 },
  planFeature: { fontSize: 12, color: 'var(--color-text-muted)' },
  totalRow: {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    background: 'var(--color-primary-dim)',
    border: '1px solid var(--color-border-bright)',
    borderRadius: 10, padding: '14px 16px', marginTop: 20,
  },
  totalLabel: {
    fontSize: 13, fontWeight: 600, color: 'var(--color-text)',
    fontFamily: 'var(--font-display)',
  },
  totalAmount: {
    fontSize: 22, fontWeight: 800, color: 'var(--color-primary)',
    fontFamily: 'var(--font-display)',
  },
  disclaimer: {
    fontSize: 11, color: 'var(--color-text-dim)',
    marginTop: 12, lineHeight: 1.5,
  },
}
