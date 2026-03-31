import React, { useState } from 'react'

export default function Input({
  label,
  name,
  type = 'text',
  placeholder,
  value,
  onChange,
  error,
  hint,
  icon,
  required,
  autoComplete,
  style: extraStyle,
  ...rest
}) {
  const [focused, setFocused] = useState(false)

  return (
    <div style={{ ...styles.group, ...extraStyle }}>
      {label && (
        <label style={styles.label}>
          {label}
          {required && <span style={styles.req}> *</span>}
        </label>
      )}
      <div style={{
        ...styles.inputWrap,
        borderColor: error
          ? 'var(--color-error)'
          : focused
          ? 'var(--color-primary)'
          : 'var(--color-border)',
        boxShadow: focused
          ? `0 0 0 3px ${error ? 'rgba(239,68,68,0.15)' : 'rgba(59,130,246,0.15)'}`
          : 'none',
      }}>
        {icon && <span style={styles.icon}>{icon}</span>}
        <input
          name={name}
          type={type}
          placeholder={placeholder}
          value={value}
          onChange={onChange}
          onFocus={() => setFocused(true)}
          onBlur={() => setFocused(false)}
          autoComplete={autoComplete}
          style={{
            ...styles.input,
            paddingLeft: icon ? 40 : 16,
          }}
          {...rest}
        />
      </div>
      {error && <p style={styles.error}>{error}</p>}
      {hint && !error && <p style={styles.hint}>{hint}</p>}
    </div>
  )
}

const styles = {
  group: {
    display: 'flex',
    flexDirection: 'column',
    gap: 6,
  },
  label: {
    fontSize: 13,
    fontWeight: 600,
    color: 'var(--color-text-muted)',
    fontFamily: 'var(--font-display)',
    letterSpacing: '0.2px',
  },
  req: {
    color: 'var(--color-error)',
  },
  inputWrap: {
    position: 'relative',
    background: 'rgba(255,255,255,0.04)',
    border: '1px solid var(--color-border)',
    borderRadius: 12,
    transition: 'all 0.2s ease',
  },
  icon: {
    position: 'absolute',
    left: 12,
    top: '50%',
    transform: 'translateY(-50%)',
    color: 'var(--color-text-dim)',
    display: 'flex',
    alignItems: 'center',
    pointerEvents: 'none',
  },
  input: {
    width: '100%',
    padding: '13px 16px',
    background: 'transparent',
    border: 'none',
    outline: 'none',
    color: 'var(--color-text)',
    fontSize: 15,
    fontFamily: 'var(--font-body)',
  },
  error: {
    fontSize: 12,
    color: 'var(--color-error)',
    fontFamily: 'var(--font-body)',
    display: 'flex',
    alignItems: 'center',
    gap: 4,
  },
  hint: {
    fontSize: 12,
    color: 'var(--color-text-dim)',
    fontFamily: 'var(--font-body)',
  },
}
