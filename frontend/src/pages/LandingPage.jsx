import React from 'react'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import PageWrapper from '../components/PageWrapper'
import Button from '../components/Button'

const features = [
  { icon: '⚡', title: 'Instant Activation', desc: 'Get activated in under 5 minutes' },
  { icon: '🔒', title: 'Secure & Verified', desc: 'Bank-grade identity verification' },
  { icon: '📶', title: 'Best Network', desc: 'Pan-India 5G coverage' },
]

export default function LandingPage() {
  const navigate = useNavigate()

  return (
    <PageWrapper>
      {/* Hero */}
      <section style={styles.hero}>
        {/* Glow orbs */}
        <div style={styles.orb1} />
        <div style={styles.orb2} />

        <div style={styles.heroContent}>
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, ease: [0.22, 1, 0.36, 1] }}
          >
            <div style={styles.badge}>
              <span style={styles.badgeDot} />
              New — 5G Plans Available
            </div>

            <h1 style={styles.headline}>
              Activate Your SIM<br />
              <span className="gradient-text">in Minutes</span>
            </h1>

            <p style={styles.sub}>
              Fast, secure and paperless SIM activation. Choose your plan,
              verify your identity, and go live — all in one seamless flow.
            </p>

            <div style={styles.ctaRow}>
              <Button size="lg" onClick={() => navigate('/validate')} icon={<ArrowIcon />}>
                Get Started
              </Button>
              <Button size="lg" variant="ghost" onClick={() => navigate('/validate')}>
                View Plans
              </Button>
            </div>

            <div style={styles.trustRow}>
              {['256-bit Encrypted', 'No Hidden Fees', 'Instant eSIM Support'].map(t => (
                <span key={t} style={styles.trustItem}>
                  <CheckIcon /> {t}
                </span>
              ))}
            </div>
          </motion.div>

          {/* SIM Illustration */}
          <motion.div
            style={styles.illustration}
            initial={{ opacity: 0, scale: 0.85, rotate: -4 }}
            animate={{ opacity: 1, scale: 1, rotate: 0 }}
            transition={{ duration: 0.8, delay: 0.2, ease: [0.22, 1, 0.36, 1] }}
          >
            <SimCard3D />
          </motion.div>
        </div>
      </section>

      {/* Features */}
      <section style={styles.features}>
        <div style={styles.featuresGrid}>
          {features.map((f, i) => (
            <motion.div
              key={f.title}
              className="glass"
              style={styles.featureCard}
              initial={{ opacity: 0, y: 24 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3 + i * 0.1, duration: 0.5 }}
              whileHover={{ y: -4, borderColor: 'var(--color-border-bright)' }}
            >
              <div style={styles.featureIcon}>{f.icon}</div>
              <h3 style={styles.featureTitle}>{f.title}</h3>
              <p style={styles.featureDesc}>{f.desc}</p>
            </motion.div>
          ))}
        </div>
      </section>

      {/* Stats */}
      <section style={styles.stats}>
        {[
          { num: '10M+', label: 'SIMs Activated' },
          { num: '99.9%', label: 'Uptime' },
          { num: '< 3 min', label: 'Avg Activation Time' },
          { num: '4.9★', label: 'Customer Rating' },
        ].map((s, i) => (
          <motion.div
            key={s.label}
            style={styles.stat}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.5 + i * 0.08 }}
          >
            <span style={styles.statNum}>{s.num}</span>
            <span style={styles.statLabel}>{s.label}</span>
          </motion.div>
        ))}
      </section>
    </PageWrapper>
  )
}

function ArrowIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
      <path d="M5 12h14M12 5l7 7-7 7"/>
    </svg>
  )
}

function CheckIcon() {
  return (
    <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3">
      <polyline points="20 6 9 17 4 12"/>
    </svg>
  )
}

function SimCard3D() {
  return (
    <div style={card.outer}>
      <div style={card.card}>
        {/* Network logo area */}
        <div style={card.header}>
          <div style={card.network}>5G</div>
          <div style={card.signal}>
            {[1,2,3,4].map(b => (
              <div key={b} style={{ ...card.bar, height: b * 5, opacity: b <= 3 ? 1 : 0.3 }} />
            ))}
          </div>
        </div>

        {/* Chip */}
        <div style={card.chipArea}>
          <div style={card.chip}>
            <div style={card.chipLine} />
            <div style={{ ...card.chipLine, width: '60%' }} />
            <div style={card.chipLine} />
          </div>
          <div style={card.nfcIcon}>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,0.5)" strokeWidth="1.5">
              <path d="M5 12.5a7 7 0 0 1 7-7"/>
              <path d="M3 12.5a9 9 0 0 1 9-9"/>
              <path d="M7 12.5a5 5 0 0 1 5-5"/>
              <circle cx="12" cy="12.5" r="1" fill="rgba(255,255,255,0.5)"/>
            </svg>
          </div>
        </div>

        {/* Number */}
        <div style={card.simNumber}>8901 2601 2345 6789</div>
        <div style={card.simLabel}>SIM Card • Nano / Micro</div>

        {/* Decorative gradient */}
        <div style={card.glow} />
      </div>

      {/* Shadow card behind */}
      <div style={card.shadow} />
    </div>
  )
}

const card = {
  outer: {
    position: 'relative',
    width: 320,
    height: 200,
  },
  card: {
    width: 320,
    height: 200,
    borderRadius: 20,
    background: 'linear-gradient(135deg, #1a3a6e 0%, #0c2051 50%, #061230 100%)',
    border: '1px solid rgba(59,130,246,0.4)',
    padding: 24,
    position: 'relative',
    overflow: 'hidden',
    boxShadow: '0 20px 60px rgba(0,0,0,0.5), 0 0 40px rgba(59,130,246,0.2)',
  },
  shadow: {
    position: 'absolute',
    top: 12, left: 12,
    width: 320, height: 200,
    borderRadius: 20,
    background: 'linear-gradient(135deg, #0c2051, #061230)',
    border: '1px solid rgba(59,130,246,0.15)',
    zIndex: -1,
    opacity: 0.6,
  },
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 16,
  },
  network: {
    fontSize: 22,
    fontWeight: 800,
    fontFamily: 'var(--font-display)',
    background: 'linear-gradient(135deg, #60a5fa, #06b6d4)',
    WebkitBackgroundClip: 'text',
    WebkitTextFillColor: 'transparent',
    letterSpacing: '-1px',
  },
  signal: {
    display: 'flex',
    alignItems: 'flex-end',
    gap: 3,
  },
  bar: {
    width: 4,
    background: 'rgba(96,165,250,0.8)',
    borderRadius: 2,
  },
  chipArea: {
    display: 'flex',
    alignItems: 'center',
    gap: 16,
    marginBottom: 20,
  },
  chip: {
    width: 44,
    height: 34,
    borderRadius: 6,
    background: 'linear-gradient(135deg, #c8a84b, #f5d78e, #c8a84b)',
    padding: '8px 6px',
    display: 'flex',
    flexDirection: 'column',
    gap: 4,
    boxShadow: '0 2px 8px rgba(0,0,0,0.3)',
  },
  chipLine: {
    height: 2,
    width: '100%',
    background: 'rgba(0,0,0,0.25)',
    borderRadius: 1,
  },
  nfcIcon: {
    opacity: 0.6,
  },
  simNumber: {
    fontSize: 13,
    letterSpacing: '2px',
    color: 'rgba(255,255,255,0.7)',
    fontFamily: 'var(--font-display)',
    fontWeight: 500,
    marginBottom: 4,
  },
  simLabel: {
    fontSize: 11,
    color: 'rgba(255,255,255,0.35)',
    fontFamily: 'var(--font-body)',
    letterSpacing: '1px',
    textTransform: 'uppercase',
  },
  glow: {
    position: 'absolute',
    top: -40, right: -40,
    width: 140, height: 140,
    borderRadius: '50%',
    background: 'radial-gradient(circle, rgba(59,130,246,0.25) 0%, transparent 70%)',
    pointerEvents: 'none',
  },
}

const styles = {
  hero: {
    position: 'relative',
    overflow: 'hidden',
    padding: '80px 24px 60px',
  },
  orb1: {
    position: 'absolute',
    top: -100, left: -100,
    width: 500, height: 500,
    borderRadius: '50%',
    background: 'radial-gradient(circle, rgba(59,130,246,0.08) 0%, transparent 70%)',
    pointerEvents: 'none',
  },
  orb2: {
    position: 'absolute',
    bottom: -100, right: -100,
    width: 400, height: 400,
    borderRadius: '50%',
    background: 'radial-gradient(circle, rgba(6,182,212,0.06) 0%, transparent 70%)',
    pointerEvents: 'none',
  },
  heroContent: {
    maxWidth: 1100,
    margin: '0 auto',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 48,
    flexWrap: 'wrap',
  },
  badge: {
    display: 'inline-flex',
    alignItems: 'center',
    gap: 8,
    background: 'rgba(59,130,246,0.1)',
    border: '1px solid rgba(59,130,246,0.25)',
    borderRadius: 100,
    padding: '6px 14px',
    fontSize: 13,
    color: 'var(--color-primary)',
    fontWeight: 600,
    fontFamily: 'var(--font-display)',
    marginBottom: 24,
    letterSpacing: '0.2px',
  },
  badgeDot: {
    width: 7, height: 7,
    borderRadius: '50%',
    background: 'var(--color-primary)',
    boxShadow: '0 0 8px var(--color-primary)',
  },
  headline: {
    fontFamily: 'var(--font-display)',
    fontSize: 'clamp(38px, 5vw, 60px)',
    fontWeight: 800,
    lineHeight: 1.1,
    color: 'var(--color-text)',
    letterSpacing: '-2px',
    marginBottom: 20,
  },
  sub: {
    fontSize: 17,
    color: 'var(--color-text-muted)',
    maxWidth: 480,
    lineHeight: 1.7,
    marginBottom: 36,
    fontFamily: 'var(--font-body)',
  },
  ctaRow: {
    display: 'flex',
    gap: 14,
    flexWrap: 'wrap',
    marginBottom: 28,
  },
  trustRow: {
    display: 'flex',
    gap: 20,
    flexWrap: 'wrap',
  },
  trustItem: {
    display: 'flex',
    alignItems: 'center',
    gap: 6,
    fontSize: 13,
    color: 'var(--color-text-muted)',
    fontFamily: 'var(--font-body)',
  },
  illustration: {
    flexShrink: 0,
  },
  features: {
    padding: '0 24px 60px',
  },
  featuresGrid: {
    maxWidth: 1100,
    margin: '0 auto',
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
    gap: 20,
  },
  featureCard: {
    padding: '28px 24px',
    transition: 'border-color 0.3s',
    cursor: 'default',
  },
  featureIcon: {
    fontSize: 32,
    marginBottom: 14,
  },
  featureTitle: {
    fontFamily: 'var(--font-display)',
    fontWeight: 700,
    fontSize: 17,
    color: 'var(--color-text)',
    marginBottom: 8,
  },
  featureDesc: {
    fontSize: 14,
    color: 'var(--color-text-muted)',
    lineHeight: 1.6,
  },
  stats: {
    maxWidth: 1100,
    margin: '0 auto',
    padding: '40px 24px 80px',
    display: 'flex',
    justifyContent: 'space-around',
    flexWrap: 'wrap',
    gap: 24,
    borderTop: '1px solid var(--color-border)',
  },
  stat: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    gap: 4,
  },
  statNum: {
    fontFamily: 'var(--font-display)',
    fontSize: 32,
    fontWeight: 800,
    background: 'linear-gradient(135deg, #60a5fa, #06b6d4)',
    WebkitBackgroundClip: 'text',
    WebkitTextFillColor: 'transparent',
    letterSpacing: '-1px',
  },
  statLabel: {
    fontSize: 13,
    color: 'var(--color-text-dim)',
    fontFamily: 'var(--font-body)',
  },
}
