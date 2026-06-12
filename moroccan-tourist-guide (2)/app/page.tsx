"use client"

import { useState } from "react"
import { ZelligePattern } from "@/components/zellige-pattern"
import { Header } from "@/components/header"
import { SideMenu } from "@/components/side-menu"
import { HeroSection } from "@/components/hero-section"
import { CategoryGrid } from "@/components/category-grid"
import { FeaturedDestinations } from "@/components/featured-destinations"
import { FootballSection } from "@/components/football-section"
import { CuisineSection } from "@/components/cuisine-section"
import { WeatherWidget } from "@/components/weather-widget"
import { TestimonialsSection } from "@/components/testimonials-section"
import { QuickActions } from "@/components/quick-actions"
import { BottomNav } from "@/components/bottom-nav"

export default function Page() {
  const [isMenuOpen, setIsMenuOpen] = useState(false)

  return (
    <div className="min-h-screen bg-background relative">
      {/* Background pattern */}
      <ZelligePattern />

      {/* Mobile frame container */}
      <div className="mobile-frame relative z-10 bg-background/95 backdrop-blur-sm overflow-hidden flex flex-col min-h-screen">
        {/* Header */}
        <Header onMenuClick={() => setIsMenuOpen(true)} />

        {/* Side menu */}
        <SideMenu isOpen={isMenuOpen} onClose={() => setIsMenuOpen(false)} />

        {/* Main content */}
        <main className="flex-1 overflow-y-auto pb-20">
          {/* Hero with search */}
          <HeroSection />

          {/* Categories */}
          <CategoryGrid />

          {/* Featured Destinations */}
          <FeaturedDestinations />

          {/* Football Section */}
          <FootballSection />

          {/* Weather */}
          <WeatherWidget />

          {/* Cuisine */}
          <CuisineSection />

          {/* Testimonials */}
          <TestimonialsSection />

          {/* Quick Actions */}
          <QuickActions />

          {/* Footer spacer for bottom nav */}
          <div className="h-4" />
        </main>

        {/* Bottom Navigation */}
        <BottomNav />
      </div>
    </div>
  )
}
