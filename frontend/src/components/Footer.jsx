import React from 'react'

export default function Footer() {
  return (
    <footer style={styles.footer}>
      <div style={styles.inner}>
        <span style={styles.copy}>© 2024 SIMPortal — Telecom Activation System</span>
        <div style={styles.links}>
          <a href="#" style={styles.link}>Privacy</a>
          <span style={styles.sep}>·</span>
          <a href="#" style={styles.link}>Terms</a>
          <span style={styles.sep}>·</span>
          <a href="#" style={styles.link}>Support</a>
        </div>
      </div>
    </footer>
  )
}

const styles = {
  footer: {
    borderTop: '1px solid var(--color-border)',
    padding: '20px 24px',
    background: 'rgba(6,13,26,0.6)',
  },
  inner: {
    maxWidth: 1100,
    margin: '0 auto',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
    gap: 12,
  },
  copy: {
    fontSize: 13,
    color: 'var(--color-text-dim)',
    fontFamily: 'var(--font-body)',
  },
  links: {
    display: 'flex',
    alignItems: 'center',
    gap: 8,
  },
  link: {
    fontSize: 13,
    color: 'var(--color-text-muted)',
    textDecoration: 'none',
    transition: 'color 0.2s',
  },
  sep: {
    color: 'var(--color-text-dim)',
    fontSize: 13,
  },
}
